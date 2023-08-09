package com.ibsystem.temifoodorder.presentation.screen.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibsystem.temifooddelivery.data.repository.OrderRepository
import com.ibsystem.temifoodorder.domain.model.InsertParam
import com.ibsystem.temifoodorder.domain.model.OrderItem
import com.ibsystem.temifoodorder.domain.model.OrderProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import com.ibsystem.temifoodorder.domain.model.ProductItem
import com.ibsystem.temifoodorder.domain.model.TableModel
import com.ibsystem.temifoodorder.utils.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val tableModel: TableModel
) : ViewModel() {


    private val _productCartList = MutableStateFlow<Map<ProductItem, OrderProduct>>(emptyMap())
    val productCartList = _productCartList.asStateFlow()

    init {}


    fun addCart(
        productItem: ProductItem,
        orderProduct: OrderProduct = OrderProduct(quantity = 1)
    ) {
        val currentCart = _productCartList.value.toMutableMap()
        val existingProduct = currentCart[productItem]

        if (existingProduct != null) {
            println("sighhhhhh")
            val updatedProduct = existingProduct.copy(quantity = existingProduct.quantity!! + 1)
            currentCart[productItem] = updatedProduct
        } else {
            println("why meeeee")
            currentCart.put(productItem, orderProduct)
            println(productItem.toString())
        }

        _productCartList.value = currentCart
    }





    fun deleteCart() {
        _productCartList.value = emptyMap()
    }



    fun insertNewOrder(tableID: String = tableModel.tableID, productCartList: Map<ProductItem, OrderProduct>) {
        viewModelScope.launch {
            orderRepository.insertNewOrder(OrderItem(tableId = tableID)).collectLatest {
                    data ->
                if(data is ApiResult.Success) {
                    Log.i("NewOrder", "DONE")
                    data.data.forEach{
                            orderItem -> addNewOrderProductDetails(orderID = orderItem.id!!, productCartList = productCartList)
                    }
                }
                else if(data is ApiResult.Error) {
                    Log.e("API",data.message!!)
                }
            }
        }
    }

    private fun addNewOrderProductDetails(orderID: String, productCartList: Map<ProductItem, OrderProduct>) {
        viewModelScope.launch {
            productCartList.forEach {
                    (productItem, orderProduct) ->
                orderRepository.addNewOrderProductDetails(
                    InsertParam(orderID = orderID, prodID = productItem.prodId!!, quantity = orderProduct.quantity!!.toString())
                )
            }
        }
    }

}