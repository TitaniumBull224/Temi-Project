package com.ibsystem.temiassistant.presentation.chat_ui

import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibsystem.temiassistant.network.openWeatherApiService
import com.ibsystem.temiassistant.network.witApiService
import com.ibsystem.temiassistant.presentation.setting_ui.SettingVMObj
import com.robotemi.sdk.Robot
import kotlinx.coroutines.launch

class ChatScreenViewModel(private val mRobot: Robot): ViewModel() {
    private val _messageList = MutableLiveData<List<Message>>(emptyList())
    val messageList: LiveData<List<Message>> = _messageList

    private val _connectivityState = MutableLiveData<Boolean>()
    val connectivityState: LiveData<Boolean> = _connectivityState

    private var _sessionID: String = generateSessionID(5)

    lateinit var longitude: String
    lateinit var latitude: String

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
        addMessage(Message(message, true))
        viewModelScope.launch {
            val response = witApiService.sendMessage(_sessionID, message)

            if (response.isSuccessful) {
                val responseMessage = response.body()
                if (responseMessage != null) {
                    Log.i("WIT AI API", responseMessage.toString())
                    robotResponse(responseMessage.response.text)
                    when (responseMessage.contextMap?.command_type) {
                        "system" -> {
                            Log.i("System Command", message.message)
                            mRobot.startDefaultNlu(message.message)
                        }
                        "get_weather" -> {
                            val weatherResponse = openWeatherApiService.getWeatherData(latitude, longitude)
                            Log.i("Weather API", weatherResponse.body().toString())
                            if (weatherResponse.isSuccessful) {
                                val weatherResponseBody = weatherResponse.body()
                                var weatherDescription = ""
                                weatherResponseBody!!.weather.forEach() {i ->
                                    weatherDescription += i.description + "　"
                                }

                                robotResponse("温度は" +
                                        weatherResponseBody.main.temp + "度\n湿度は" +
                                        weatherResponseBody.main.humidity + "%\n天気予報：" +
                                        weatherDescription + "\nロケーション: " +
                                        weatherResponseBody.name
                                )
                            }
                        }
                        else -> Log.i("Weather API", "わがんない")
                    }
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

    private fun robotResponse(speech: String) {
        addMessage(Message(MessageBody(speech), false))
        if(SettingVMObj.SettingVM.isSpeakerOn.value) {
            mRobot.askQuestion(speech)
        }

    }
}