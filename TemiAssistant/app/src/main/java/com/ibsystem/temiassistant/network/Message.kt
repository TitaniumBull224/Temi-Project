package com.ibsystem.temiassistant.network

data class Message(
    val contextMap: ContextMap,
    val expectsInput: Boolean,
    val isFinal: Boolean,
    val response: Response,
)

data class ContextMap(
    val name: String,
)

data class Response(
    val speech: Speech,
    val text: String,
)

data class Speech(
    val q: String,
    val voice: String,
)