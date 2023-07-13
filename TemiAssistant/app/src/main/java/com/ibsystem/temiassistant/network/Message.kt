package com.ibsystem.temiassistant.network

import java.util.Calendar
import java.util.Date

data class Message(
    var time: Date = Calendar.getInstance().time,
    val message_body: MessageBody,
    val isOut: Boolean
)

data class MessageBody(
    val type: String = "message",
    val message: String
)

///////////////

data class ResponseMessage(
    val contextMap: ContextMap,
    val expectsInput: Boolean,
    val isFinal: Boolean,
    val response: ResponseBody,
    val isOut: Boolean
)

data class ContextMap(
    val name: String,
)

data class ResponseBody(
    val speech: Speech,
    val text: String,
)

data class Speech(
    val q: String,
    val voice: String,
)