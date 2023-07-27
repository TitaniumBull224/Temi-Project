package com.ibsystem.temiassistant.presentation.chat_ui


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibsystem.temiassistant.network.currencyApiService
import com.ibsystem.temiassistant.network.newsApiService
import com.ibsystem.temiassistant.network.openWeatherApiService
import com.ibsystem.temiassistant.network.wikiApiService
import com.ibsystem.temiassistant.network.witApiService
import com.robotemi.sdk.Robot
import com.robotemi.sdk.navigation.model.Position
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.sqrt

class ChatScreenViewModel: ViewModel() {
    private val mRobot = Robot.getInstance()

    private val _messageList = MutableLiveData<List<Message>>(emptyList())
    val messageList: LiveData<List<Message>> = _messageList

    private val _connectivityState = MutableLiveData<Boolean>()
    val connectivityState: LiveData<Boolean> = _connectivityState

    private var _currentPosition: Position = Position()

    private var _sessionID: String = generateSessionID(5)

    lateinit var longitude: String
    lateinit var latitude: String

    private val _convertShowDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val convertShowDialog = _convertShowDialog.asStateFlow()

    private suspend fun addMessage(message: Message) {
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

    fun setCurrentPosition(position: Position) {
        _currentPosition = position
    }

    fun converShowDialogSwitch() {
        _convertShowDialog.value = _convertShowDialog.value.not()
    }

    fun messageToWit(message: MessageBody) {
        viewModelScope.launch {
            addMessage(Message(message, true))
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
                            val weatherResponse =
                                openWeatherApiService.getWeatherData(latitude, longitude)
                            Log.i("Weather API", weatherResponse.body().toString())
                            if (weatherResponse.isSuccessful) {
                                val weatherResponseBody = weatherResponse.body()
                                var weatherDescription = ""
                                weatherResponseBody!!.weather.forEach { i ->
                                    weatherDescription += i.description + "　"
                                }

                                robotResponse(
                                    "温度は" +
                                            weatherResponseBody.main.temp + "度\n湿度は" +
                                            weatherResponseBody.main.humidity + "%\n天気予報：" +
                                            weatherDescription + "\nロケーション: " +
                                            weatherResponseBody.name
                                )
                            }
                        }

                        "movement" -> {
                            responseMessage.contextMap.direction?.let {
                                Log.i("Movement", it)
                                robotMove(it)

                            }
                        }
                        "wiki_query" -> {
                            val wikiQueryResponse = responseMessage.contextMap.query_key?.let {
                                wikiApiService.getPageSummary(it)
                            }
                            if (wikiQueryResponse != null) {
                                if(wikiQueryResponse.isSuccessful) {
                                    wikiQueryResponse.body()?.extract?.let {
                                        Log.i("WIKI", it)
                                        robotResponse("Wikiによって、$it", wikiQueryResponse.body()!!.thumbnail?.source)
                                    }
                                    wikiQueryResponse.body()?.contentUrls?.desktop?.page?.let {
                                        MessageBody("ソース：$it")
                                    }?.let { Message(it, false) }?.let { addMessage(it) }
                                }
                            }
                        }
                        "get_news" -> {
                            val newsResponse = newsApiService.getHeadlineNews()
                            if(newsResponse.isSuccessful) {
                                val newsResponseBody = newsResponse.body()
                                for(article in newsResponseBody!!.articles!!) {
                                    robotResponse(article!!.title!! + "\n" + article.description, article.urlToImage)
                                }
                            }
                        }
                        "convert_currency" -> {
                            converShowDialogSwitch()
                        }

                        else -> Log.i("CONTEXT", "UNKNOWN")
                    }
                }
            } else {
                robotResponse("Error: Code ${response.code()}")
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

    suspend fun convertCurrency(from: String, to: String, amount: String) {
        Log.i("Convert", "$amount // $from -> $to")
        val convertResponse = currencyApiService.convertCurrency(from, to, amount)
        if(convertResponse.isSuccessful) {
            robotResponse(convertResponse.body()!!.amount.toString())
        }
    }

    private fun robotResponse(speech: String, imageUrl: String? = null) {
        viewModelScope.launch {
            mRobot.askQuestion(speech)
            when (imageUrl) {
                null -> addMessage(Message(MessageBody(speech), false))
                else -> addMessage(Message(MessageBody(speech), false, imageUrl = imageUrl))
            }
        }
    }


    private fun robotMove(direction: String, distance: Float = 1.0F) {
        val newPosition: Position = _currentPosition
        Log.i("RobotMove", "Before X: ${newPosition.x}, Y: ${newPosition.y}")
        when {
            direction.contains("斜め") -> {
                when {
                    direction.contains("前") -> {
                        newPosition.x += (distance / sqrt(2.0)).toFloat()
                        newPosition.y += (distance / sqrt(2.0)).toFloat()
                    }
                    direction.contains("後") -> {
                        newPosition.x -= (distance / sqrt(2.0)).toFloat()
                        newPosition.y -= (distance / sqrt(2.0)).toFloat()
                    }
                }
            }
            direction.contains("前") -> newPosition.x += distance
            direction.contains("後") -> newPosition.x -= distance
            direction.contains("右") -> newPosition.y += distance
            direction.contains("左") -> newPosition.y -= distance


            direction.contains("横") -> newPosition.y += distance
            direction.contains("縦") -> newPosition.x += distance
        }
        Log.i("RobotMove", "After X: ${newPosition.x}, Y: ${newPosition.y}")
        mRobot.goToPosition(newPosition)
    }
}