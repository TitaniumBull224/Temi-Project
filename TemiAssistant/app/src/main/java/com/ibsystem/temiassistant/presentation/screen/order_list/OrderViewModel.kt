package com.ibsystem.temiassistant.presentation.screen.order_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ibsystem.temiassistant.data.datasource.ApiResult
import com.ibsystem.temiassistant.data.repository.OrderRepository
import com.ibsystem.temiassistant.data.repository.OrderRepositoryImpl
import com.ibsystem.temiassistant.domain.model.OrderModelItem
import com.ibsystem.temiassistant.domain.model.OrderProduct
import com.ibsystem.temiassistant.navigation.Screen
import com.robotemi.sdk.Robot
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.decodeRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor (private val repository: OrderRepository): ViewModel() {
    val mRobot = Robot.getInstance()
    private val locationList: List<String> = mRobot.locations

    private val _uiState = MutableStateFlow<ApiResult<*>>(ApiResult.Loading)
    val uiState: StateFlow<ApiResult<*>>
        get() = _uiState

    private val _orderList = MutableStateFlow<List<OrderModelItem>>(mutableListOf())
    val orderList: StateFlow<List<OrderModelItem>>
        get() = _orderList

    private val _checkedOrderList = MutableStateFlow<List<String>>(mutableListOf())
    val checkedOrderList: StateFlow<List<String>>
        get() = _checkedOrderList

    init {
        getAllOrders()
        if (repository.getRealtimeStatus() == Realtime.Status.DISCONNECTED) {
            listenToOrdersChange()
        }
    }

    private fun getAllOrders() {
        viewModelScope.launch {
            repository.getAllOrders().collectLatest {
                data -> _uiState.update { data }
                 if (data is ApiResult.Success) {
                     val orderList = data.data
                     _orderList.value = orderList
                 }
            }
        }
    }

    private fun listenToOrdersChange() {
        viewModelScope.launch {
            repository.listenToOrdersChange().collect {
                when (it) {
                    is PostgresAction.Delete -> Log.i("Listener","Deleted: ${it.oldRecord}")
                    is PostgresAction.Insert -> {
                        Log.i("Listener", "Inserted: ${it.record["id"]}")
                        addNewOrders(getOrderID(it))
                    }
                    is PostgresAction.Select -> Log.i("Listener","Selected: ${it.record}")
                    is PostgresAction.Update -> {
                        Log.i("Listener", "Updated: ${it.oldRecord} with ${it.record}")
                        updateOrderList(it.record["id"].toString().replace("\"", ""))
                    }
                    else -> Log.i("Listener","ERROR")
                }
            }
        }
    }

    private fun getOrderID(it: PostgresAction.Insert) =
        it.record["id"].toString().replace("\"", "")

    private fun addNewOrders(id: String) {
        viewModelScope.launch {
            repository.getOrderDetailsByID(id).collectLatest { res ->
                if (res is ApiResult.Success) {
                    _orderList.value = _orderList.value + res.data
                } else {
                    Log.i("addNewOrders",res.toString())
                }
            }
        }
    }

    private fun updateOrderList(id: String) {
        viewModelScope.launch {
            repository.getOrderDetailsByID(id = id).collectLatest { res ->
                _uiState.update { res }
                if (res is ApiResult.Success) {
                    val orderIndex = _orderList.value.indexOfFirst { it.id == id }
                    if (orderIndex != -1) {
                        // Create a new list by updating the order status
                        val updatedOrderList = _orderList.value.toMutableList()
                        updatedOrderList[orderIndex] = res.data

                        // Update the state flow with the updated order list
                        _orderList.emit(updatedOrderList)
                    }
                } else {
                    Log.i("updateOrderList", res.toString())
                }

            }

        }
    }

    fun updateOrderStatus(id: String, newStatus: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(id, newStatus).collectLatest { res ->
                _uiState.update { res }
                val orderIndex = _orderList.value.indexOfFirst { it.id == id }
                if (orderIndex != -1) {
                    // Create a new list by updating the order status
                    val updatedOrderList = _orderList.value.toMutableList()
                    updatedOrderList[orderIndex] = updatedOrderList[orderIndex].copy(status = newStatus)

                    // Update the state flow with the updated order list
                    _orderList.emit(updatedOrderList)
                }
            }
        }
    }

    fun addCheckedOrderList(orderID: String) {
        _checkedOrderList.value += orderID
    }

    fun removeCheckedOrderList(orderID: String) {
        _checkedOrderList.value -= orderID
    }

    fun clearCheckedOrderList() {
        _checkedOrderList.value = emptyList()
    }

    fun processCheckedRow(navController: NavController) {
        if (_checkedOrderList.value.isNotEmpty()) {
            val id = _checkedOrderList.value.first()
            updateOrderStatus(id, "準備完了")

            val tableID = findOrderByID(id)!!.tableId!!
            if (locationList.contains(tableID)) {
                mRobot.askQuestion("行ってきます")
                mRobot.goTo(location = tableID)
            } else {
                Log.i("GOTO", "Location Not Found!")
            }
            Log.i("checkedOrder", "/$id")
            navController.navigate(Screen.CustomerScreen.route + "/$id")

            _checkedOrderList.value -= id
        }
    }

    fun findOrderByID(ID: String): OrderModelItem? {
        val order = _orderList.value.firstOrNull { it.id == ID }
        if (order == null) {
            Log.i("findOrderByID", "No result")
        }

        return order
    }

}