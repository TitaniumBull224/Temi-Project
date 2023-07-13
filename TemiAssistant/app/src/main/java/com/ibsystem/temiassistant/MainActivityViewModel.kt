package com.ibsystem.temiassistant

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibsystem.temiassistant.network.Message
import com.ibsystem.temiassistant.network.MessageBody
import com.ibsystem.temiassistant.network.witApiService
import kotlinx.coroutines.launch
import java.util.Date

class MainActivityViewModel: ViewModel() {
    private val message_list: MutableList<Message> = mutableListOf()
    val messages: List<Message> = message_list

    private val connectivityState = mutableStateOf(true)
    val _connectivityState = connectivityState

    fun addMessage(message: Message) {
        message_list.add(0, message)
    }

    fun messageToWit(message: MessageBody) {
        addMessage(Message(message_body = message, isOut = true))
        viewModelScope.launch {
            val response = witApiService.sendMessage(message = message)
            if (response.code() == 200 && response.body() !=null && response.body()!!.size >0) {
                response.body()!!.forEach() { response_mess ->
                    addMessage(Message(message_body = MessageBody(message = response_mess.response.text), isOut = false))
                }
            }
                else
                {
                    addMessage(Message(message_body = MessageBody(message = "Error: Code ${response.code()}"), isOut = false))
                }
            }
        }

    fun changeConnectivityState(currentConnectivityState: Boolean) {
        connectivityState.value = currentConnectivityState
    }

}