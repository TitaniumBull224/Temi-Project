package com.ibsystem.temiassistant.presentation.chat_ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibsystem.temiassistant.network.witApiService
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import kotlinx.coroutines.launch

class ChatScreenViewModel(private val mRobot: Robot): ViewModel() {
    private val _messageList = MutableLiveData<List<Message>>(emptyList())
    val messageList: LiveData<List<Message>> = _messageList

    private val _connectivityState = MutableLiveData<Boolean>()
    val connectivityState: LiveData<Boolean> = _connectivityState

    fun addMessage(message: Message) {
        val currentList = _messageList.value.orEmpty()
        _messageList.value = currentList + message
    }

    fun clearMessage() {
        _messageList.value = emptyList()
    }

    fun changeConnectivityState(currentConnectivityState: Boolean) {
        _connectivityState.value = currentConnectivityState
    }

    fun messageToWit(message: MessageBody) {
        addMessage(Message(message_body = message, isOut = true))
        viewModelScope.launch {
            val response = witApiService.sendMessage(message = message)

            if (response.isSuccessful) {
                val responseMessage = response.body()
                if (responseMessage != null) {
                    Log.i(response.code().toString(), responseMessage.toString())
//                    val ttsRequest = TtsRequest.create(responseMessage.response.text, true)
//                    mRobot.speak(ttsRequest)
                    addMessage(Message(MessageBody(responseMessage.response.text), false))
                    mRobot.askQuestion(responseMessage.response.text)
                }
            } else {
                addMessage(Message(MessageBody("Error: Code ${response.code()}"), false))
            }

//            if (response.code() == 200 && response.body() != null && response.body()!!.size > 0) {
//                response.body()!!.forEach() { response_mess ->
//                    addMessage(Message(MessageBody(response_mess.response.text), false))
//                }
//            }  else {
//                addMessage(Message(MessageBody("Error: Code ${response.code()}"), false))
//            }
        }
    }
}