package com.ibsystem.temifoodorder.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderDetailItem(
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
    val time: String?,
    @SerialName("total_item")
    val total_item : Int?
)

@Serializable
data class OrderProduct(
    @SerialName("quantity")
    val quantity: Int?
)

@Serializable
data class OrderItem(
    @SerialName("id")
    val id: String? = null,
    @SerialName("time")
    val time: String? = null,
    @SerialName("status")
    val status: String?,
    @SerialName("table_id")
    val tableId: String?,
    @SerialName("total_item")
    val total_item : Int?
)

