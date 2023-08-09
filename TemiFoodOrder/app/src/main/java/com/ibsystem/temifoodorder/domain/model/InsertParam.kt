package com.ibsystem.temifoodorder.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InsertParam (
        @SerialName("order_id")
        val orderID: String,
        @SerialName("prod_id")
        val prodID: String,
        @SerialName("quantity")
        val quantity: String)