package com.ibsystem.temiassistant.domain.model

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class ConvertResponse(
    @Json(name = "amount")
    val amount: Int?,
    @Json(name = "date")
    val date: String?,
    @Json(name = "from")
    val from: String?,
    @Json(name = "meta")
    val meta: Meta?,
    @Json(name = "response")
    val response: Response?,
    @Json(name = "timestamp")
    val timestamp: Int?,
    @Json(name = "to")
    val to: String?,
    @Json(name = "value")
    val value: Double?
)

@JsonClass(generateAdapter = true)
data class Meta(
    @Json(name = "code")
    val code: Int?,
    @Json(name = "disclaimer")
    val disclaimer: String?
)

@JsonClass(generateAdapter = true)
data class Response(
    @Json(name = "amount")
    val amount: Int?,
    @Json(name = "date")
    val date: String?,
    @Json(name = "from")
    val from: String?,
    @Json(name = "timestamp")
    val timestamp: Int?,
    @Json(name = "to")
    val to: String?,
    @Json(name = "value")
    val value: Double?
)

@JsonClass(generateAdapter = true)
data class ConvertQuery(
    val amount: String,
    val from: String,
    val to: String
)