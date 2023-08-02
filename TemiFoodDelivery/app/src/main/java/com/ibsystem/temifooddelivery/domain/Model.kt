package com.ibsystem.temifooddelivery.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName



@Serializable
data class OrderModelItem(
    @SerialName("id")
    val id: String?,
    @SerialName("Order_Product")
    val orderProduct: List<OrderProduct?>?,
    @SerialName("Product")
    val product: List<Product>?,
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

@Serializable
data class Product(
    @SerialName("cat_id")
    val catId: String?,
    @SerialName("prod_avail")
    val prodAvail: Boolean?,
    @SerialName("prod_desc")
    val prodDesc: String?,
    @SerialName("prod_id")
    val prodId: String?,
    @SerialName("prod_image")
    val prodImage: String?,
    @SerialName("prod_name")
    val prodName: String?,
    @SerialName("prod_price")
    val prodPrice: Int?
)