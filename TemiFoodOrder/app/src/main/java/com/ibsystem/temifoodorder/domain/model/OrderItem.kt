package com.ibsystem.temifoodorder.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderModelItem(
    @SerialName("id")
    val id: String?,
    @SerialName("Order_Product")
    val orderProduct: List<OrderProduct?>?,
    @SerialName("Product")
    val product: List<ProductItem>?,
    @SerialName("status")
    val status: String?,
    @SerialName("table_id")
    val tableId: String?,
    @SerialName("time")
    val time: String?
)

@Serializable
data class OrderProduct(
    @SerialName("quantity")
    val quantity: Int?
)

