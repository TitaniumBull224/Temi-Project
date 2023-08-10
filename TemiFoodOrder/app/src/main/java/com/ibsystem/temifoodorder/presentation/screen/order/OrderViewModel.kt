package com.ibsystem.temifoodorder.presentation.screen.order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibsystem.temifooddelivery.data.repository.OrderRepository
import com.ibsystem.temifoodorder.domain.model.InsertParam
import com.ibsystem.temifoodorder.domain.model.OrderItem
import com.ibsystem.temifoodorder.domain.model.OrderDetailItem
import com.ibsystem.temifoodorder.domain.model.OrderProduct
import com.ibsystem.temifoodorder.domain.model.ProductItem
import com.ibsystem.temifoodorder.domain.model.TableModel
import com.ibsystem.temifoodorder.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.Realtime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor (private val repository: OrderRepository,
                                          private val tableModel: TableModel): ViewModel() {

    private val _uiState = MutableStateFlow<ApiResult<*>>(ApiResult.Loading)
    val uiState: StateFlow<ApiResult<*>>
        get() = _uiState

    private val _orderList = MutableStateFlow<List<OrderDetailItem>>(mutableListOf())
    val orderList: StateFlow<List<OrderDetailItem>>
        get() = _orderList



    init {
        getAllOrders()
        if(repository.getRealtimeStatus() == Realtime.Status.DISCONNECTED) {
            listenToOrdersChange()
        }
    }


    fun getAllOrders() {
        viewModelScope.launch {
            repository.getAllOrders(tableModel.tableID).collect {
                    data -> _uiState.update { data }
                if(data is ApiResult.Success) {
                    val orderList = data.data
                    _orderList.value = orderList
                }
                else if(data is ApiResult.Error) {
                    Log.e("API",data.message!!)
                }
            }
        }
        //listenToOrdersChange()
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

    private fun addNewOrders(id: String) {
        viewModelScope.launch {
            repository.getOrderDetailsByID(id).collectLatest { res ->
                if (res is ApiResult.Success) {
                    _orderList.value = _orderList.value + res.data
                } else {
                    Log.i("NewOrders",res.toString())
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

    fun findOrderByID(ID: String): OrderDetailItem? {
        val order = _orderList.value.firstOrNull { it.id == ID }
        if (order == null) {
            Log.i("findOrderByID", "No result")
        }

        return order
    }

//    fun addCart(
//        productItem: ProductItem,
//        orderProduct: OrderProduct = OrderProduct(quantity = 1)
//    ) {
//        viewModelScope.launch {
//            _productCartList.update { cart ->
//                val updatedCart = cart.toMutableMap()
//                val existingOrderProduct = updatedCart[productItem]
//
//                if (existingOrderProduct != null) {
//                    val existingQuantity = existingOrderProduct.quantity
//                    val updatedQuantity = existingQuantity!!.plus(orderProduct.quantity!!)
//                    updatedCart[productItem] = existingOrderProduct.copy(quantity = updatedQuantity)
//                } else {
//                    updatedCart[productItem] = orderProduct
//                }
//
//                updatedCart.toMap() // Convert back to immutable map before returning
//            }
//        }
//    }
//
//
//
//    fun deleteCart() {
//        _productCartList.value = emptyMap()
//    }

}