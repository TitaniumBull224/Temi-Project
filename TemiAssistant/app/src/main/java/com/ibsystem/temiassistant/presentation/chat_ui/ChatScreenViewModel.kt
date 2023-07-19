package com.ibsystem.temiassistant.presentation.chat_ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibsystem.temiassistant.network.openWeatherApiService
import com.ibsystem.temiassistant.network.witApiService
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import kotlinx.coroutines.launch
import kotlin.random.Random

class ChatScreenViewModel(private val mRobot: Robot): ViewModel() {
    private val _messageList = MutableLiveData<List<Message>>(emptyList())
    val messageList: LiveData<List<Message>> = _messageList

    private val _connectivityState = MutableLiveData<Boolean>()
    val connectivityState: LiveData<Boolean> = _connectivityState

    private var _sessionID: String = generateSessionID(5)

    fun addMessage(message: Message) {
        val currentList = _messageList.value.orEmpty()
        _messageList.value = currentList + message
    }

    fun clearMessage() {
        _messageList.value = emptyList()
    }

    private fun generateSessionID(length: Int): String {
        val allowedChars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun newSessionID() {
        _sessionID = generateSessionID(5)
    }

    fun getSessionID(): String {
        return _sessionID
    }

    fun changeConnectivityState(currentConnectivityState: Boolean) {
        _connectivityState.value = currentConnectivityState
    }

    fun messageToWit(message: MessageBody) {
        addMessage(Message(message_body = message, isOut = true))
        viewModelScope.launch {
            val response = witApiService.sendMessage(message = message, session_id = _sessionID)

            if (response.isSuccessful) {
                val responseMessage = response.body()
                if (responseMessage != null) {
                    Log.i(response.code().toString(), responseMessage.toString())
//                    val ttsRequest = TtsRequest.create(responseMessage.response.text, true)
//                    mRobot.speak(ttsRequest)
                    addMessage(Message(MessageBody(responseMessage.response.text), false))
                    when(responseMessage.contextMap?.command_type){
                        //"system" ->
                        "get_temp" -> {
                            val weatherResponse = openWeatherApiService.getWeatherData("37.916191", "139.036407")
                            print(weatherResponse.toString())
                        }
                    }
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