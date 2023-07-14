package com.ibsystem.temiassistant.presentation.chat_ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibsystem.temiassistant.network.witApiService
import kotlinx.coroutines.launch

class ChatScreenViewModel: ViewModel() {
    private val _messageList = MutableLiveData<List<Message>>(emptyList())
    val messageList: LiveData<List<Message>> = _messageList

    private val _connectivityState = MutableLiveData<Boolean>()
    val connectivityState: LiveData<Boolean> = _connectivityState

    fun addMessage(message: Message) {
        val currentList = _messageList.value.orEmpty()
        _messageList.value = currentList + message
    }

    fun messageToWit(message: MessageBody) {
        addMessage(Message(message_body = message, isOut = true))
        viewModelScope.launch {
            val response = witApiService.sendMessage(message = message)
            if (response.code() == 200 && response.body() != null && response.body()!!.size > 0) {
                response.body()!!.forEach() { response_mess ->
                    addMessage(Message(MessageBody(response_mess.response.text), false))
                }
            }  else {
                addMessage(Message(MessageBody("Error: Code ${response.code()}"), false))
            }
        }
    }

    fun changeConnectivityState(currentConnectivityState: Boolean) {
        _connectivityState.value = currentConnectivityState
    }
}