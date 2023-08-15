package com.ibsystem.temiassistant.domain.model

import com.google.gson.JsonObject
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@JsonClass(generateAdapter = true)
data class Message(
    val message_body: MessageBody,
    val isOut: Boolean,
    var time: String = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Calendar.getInstance().time),
    var imageUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class MessageBody(
    @field:Json(name = "message")
    val message: String,
    @field:Json(name = "type")
    val type: String = "message"
)

///////////////
@JsonClass(generateAdapter = true)
data class ResponseMessage(
    @Json(name = "context_map")
    val contextMap: ContextMap?,
    @Json(name = "expects_input")
    val expectsInput: Boolean?,
    @Json(name = "is_final")
    val isFinal: Boolean?,
    @Json(name = "response")
    val response: ResponseBody,
    val isOut: Boolean?
)
@JsonClass(generateAdapter = true)
data class ContextMap(
//    @Json(name = "name")
//    val name: Name?,
    @Json(name = "command_type")
    val command_type: String?,
    @Json(name = "direction")
    val direction: String?,
    @Json(name = "query_key")
    val query_key: String?
)
@JsonClass(generateAdapter = true)
data class ResponseBody(
    val speech: Speech?,
    val text: String,
)
@JsonClass(generateAdapter = true)
data class Speech(
    val q: String?,
    val voice: String?,
)