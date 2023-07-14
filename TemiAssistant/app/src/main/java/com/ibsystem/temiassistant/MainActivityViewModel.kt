package com.ibsystem.temiassistant

import androidx.lifecycle.ViewModel
import com.ibsystem.temiassistant.network.MessageModel

class MainActivityViewModel: ViewModel() {
    private val message_list = mutableListOf<MessageModel>()
    val messages: MutableList<MessageModel> = message_list

    fun addMessage(messageModel: MessageModel){
        message_list.add(0,messageModel)
    }
}