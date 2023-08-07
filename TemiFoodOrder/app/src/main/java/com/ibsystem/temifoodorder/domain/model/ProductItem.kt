package com.ibsystem.temifoodorder.domain.model
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName


@Serializable
data class ProductItem(
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