package com.ibsystem.temiassistant.network

data class MessageModel(
    val message: String,
    val time: String,
    val isOutgoing: Boolean
)