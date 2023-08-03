package com.ibsystem.temifooddelivery.presentation.screen.order_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibsystem.temifooddelivery.data.datasource.ApiResult
import com.ibsystem.temifooddelivery.data.repository.OrderRepository
import com.ibsystem.temifooddelivery.data.repository.OrderRepositoryImpl
import com.ibsystem.temifooddelivery.domain.OrderModelItem
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.decodeRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor (
    private val repository: OrderRepository): ViewModel() {

        private val _uiState = MutableStateFlow<ApiResult<*>>(ApiResult.Loading)
        val uiState: StateFlow<ApiResult<*>>
            get() = _uiState

        private val _orderList = MutableStateFlow<List<OrderModelItem>>(mutableListOf())
        val orderList: StateFlow<List<OrderModelItem>>
            get() = _orderList

    init {
        getAllOrders()
        listenToOrdersChange()
    }


    fun getAllOrders() {
        viewModelScope.launch {
            repository.getAllOrders().collectLatest {
                data -> _uiState.update { data }
                 if(data is ApiResult.Success) {
                     val orderList = data.data
                     _orderList.value = orderList
                 }
            }
        }
    }

    fun listenToOrdersChange() {
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
                    }
                else -> Log.i("Listener","sighhh")
                }
            }
        }
    }

    private fun getOrderID(it: PostgresAction.Insert) =
        it.record["id"].toString().replace("\"", "")

    fun addNewOrders(id: String) {
        viewModelScope.launch {
            repository.getOrderDetailsByID(id).collectLatest {
                res -> if(res is ApiResult.Success) {
                    Log.i("DSDSAD","PLSSSSS")
                _orderList.value = _orderList.value + res.data
                }
                else {
                Log.i("DSDSAD",res.toString())
                }
            }
        }

    }

    fun updateOrderStatus(id: String, newStatus: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(id, newStatus).collectLatest {
                    res ->
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
}