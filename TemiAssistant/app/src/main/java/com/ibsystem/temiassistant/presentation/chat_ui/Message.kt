package com.ibsystem.temiassistant.presentation.chat_ui

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class Message(
    val message_body: MessageBody,
    val isOut: Boolean,
    var time: String = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Calendar.getInstance().time)
)

data class MessageBody(
    val message: String,
    val type: String = "message"
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