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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
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

    private val _productCartList = MutableStateFlow<Map<ProductItem, OrderProduct>>(emptyMap())
    val productCartList = _productCartList.asStateFlow()



    init {
        getAllOrders()
        if(repository.getRealtimeStatus() == Realtime.Status.DISCONNECTED) {
            listenToOrdersChange()
        }
    }


    private fun getAllOrders() {
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

    private fun listenToOrdersChange() {
        viewModelScope.launch {
            repository.listenToChange(channelName = "OrderChanged", tableName = "Order").collect {
                when (it) {
                    is PostgresAction.Delete -> Log.i("Listener","Deleted: ${it.oldRecord}")
                    is PostgresAction.Insert -> {
                        Log.i("Listener", "Inserted: ${it.record["id"]}")
                        val orderId = getOrderID(it)
//                        addNewOrderProductDetails(orderId, productCartList.value)
//                        deleteCart()
                        addNewOrders(orderId)
                        updateOrderList(it.record["id"].toString().replace("\"", ""))

                    }
                    is PostgresAction.Select -> Log.i("Listener","Selected: ${it.record}")
                    is PostgresAction.Update -> {
                        Log.i("Listener", "Updated: ${it.oldRecord} with ${it.record}")
                        updateOrderList(it.record["id"].toString().replace("\"", ""))
                    }
                    else -> Log.i("Listener","Error")
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
                    Log.i("DSDSAD",res.toString())
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

    fun addCart(
        productItem: ProductItem,
        orderProduct: OrderProduct = OrderProduct(quantity = 1)
    ) {
        viewModelScope.launch {
            val currentCart = _productCartList.value.toMutableMap()
            val existingProduct = currentCart[productItem]

            if (existingProduct != null) {
                val updatedProduct = existingProduct.copy(quantity = existingProduct.quantity!! + 1)
                currentCart[productItem] = updatedProduct
            } else {
                currentCart[productItem] = orderProduct
                println("CART: ${currentCart.toString()}")
            }

            _productCartList.update { currentCart.toMap() }
            println("SIGH"+productCartList.value.toString())
        }

    }



    fun deleteCart() {
        _productCartList.value = emptyMap()
    }



    fun insertNewOrder(tableID: String = tableModel.tableID, productCartList: Map<ProductItem, OrderProduct>) {
        viewModelScope.launch {
            repository.insertNewOrder(OrderItem(tableId = tableID, status = "保留中")).collect {
                    data ->
                if(data is ApiResult.Success) {
                    Log.i("NewOrder", "DONE")
                    data.data.forEach{
                            orderItem ->
                        addNewOrderProductDetails(
                            orderID = orderItem.id!!,
                            productCartList = productCartList
                        )
                    }
                    deleteCart()
                }
                else if(data is ApiResult.Error) {
                    Log.e("API",data.message!!)
                }
            }
        }
    }

    private suspend fun addNewOrderProductDetails(orderID: String, productCartList: Map<ProductItem, OrderProduct>) {
        coroutineScope { // Use a CoroutineScope to manage launched coroutines
            productCartList.forEach { (productItem, orderProduct) ->
                launch {
                    repository.addNewOrderProductDetails(
                        InsertParam(orderID = orderID, prodID = productItem.prodId!!, quantity = orderProduct.quantity!!.toString())
                    ).collect { data ->
                        if (data is ApiResult.Success) {
                            Log.i("ORDERDETAILS", "DONE")
                        } else if (data is ApiResult.Error) {
                            Log.e("API DETAILS", data.message!!)
                        }
                    }
                }
            }
        }
        // Now that all addNewOrderProductDetails calls are awaited, you can proceed with the subsequent functions
    }
}