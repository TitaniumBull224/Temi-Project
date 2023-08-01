package com.ibsystem.temiassistant.presentation.screen.chat


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibsystem.temiassistant.domain.model.Message
import com.ibsystem.temiassistant.domain.model.MessageBody
import com.ibsystem.temiassistant.network.currencyApiService
import com.ibsystem.temiassistant.network.newsApiService
import com.ibsystem.temiassistant.network.openWeatherApiService
import com.ibsystem.temiassistant.network.wikiApiService
import com.ibsystem.temiassistant.network.witApiService
import com.robotemi.sdk.Robot
import com.robotemi.sdk.navigation.model.Position
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _conversationStage: MutableStateFlow<Int> = MutableStateFlow(GREETING_STATUS)
    val conversationStage: StateFlow<Int> = _conversationStage // How to use this: val selectedProduct by ChatScreenViewModel.conversationStage.collectAsState()

    // Conversation Status
    companion object {
        const val GREETING_STATUS = 0
        const val INTRODUCTION_STATUS = 1
        const val USECASE_STATUS = 2
        const val END_CONVERSATION_STATUS = 99
    }

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
                    _conversationStage.value = when (responseMessage.response.text) {
                        "了解です、ご利用ありがとうございます", "すみませんが、適当スキルが見つかりません" -> GREETING_STATUS
                        "こんにちは、なんて呼んだらいいですか？", "すみませんが、よく聞こえませんでした、もう一度お願いします" -> INTRODUCTION_STATUS
                        else -> USECASE_STATUS
                    }

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
            robotResponse("結果：" + convertResponse.body()!!.value.toString())
        } else {
            Log.i("Convert", "Where did I go wrong!")
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

    fun generateSuggestions(message: String, numberOfSuggestions: Int): List<String> {
        val allSuggestions = when (_conversationStage.value) {
            GREETING_STATUS -> listOf("おはようございます", "元気ですか？", "久しぶりですね！", "こんにちは", "こんばんは")
            INTRODUCTION_STATUS -> listOf("私は山田です", "佐藤と申します", "鈴木を呼んでください", "私の名前は田中です", "私は加藤と申します")
            USECASE_STATUS -> when {
                message.isEmpty() -> listOf("天気予報", "ニュース", "為替レート", "AIとは", "充電", "追従", "YouTube", "音量上げ")
                message.contains("天気") -> listOf("今日の天気はどうですか？", "今日は暑いですね", "天気予報を教えて", "明日の天気は？", "週末の天気は？")
                message.contains("移動") -> listOf("前に向いてください", "後ろに移動してください", "左に曲がって", "右に曲がって", "止まって")
                message.contains("とは") -> listOf("林檎とは", "東京とは", "kotlinの定義を教えてください", "AIとは何ですか？", "ビットコインとは何ですか？")
                message.contains("ニュース") -> listOf("最新のニュースは何ですか？", "特定のトピックのニュースを教えてください", "今日のニュースは", "スポーツニュースを教えてください", "政治ニュースを教えてください")
                message.contains("ドル") || message.contains("円") -> listOf("為替レートは何ですか？", "両替する方法を教えてください", "両替手数料はいくらですか？", "ドルから円に換算するには？", "円からドルに換算するには？")
                message.contains("音量") || message.contains("静") -> listOf("音量を上げて", "音量を下がって", "静かにして下さい", "ミュートしてください", "音量を最大にしてください")
                message.contains("ベース") || message.contains("チャージ") -> listOf("ベースに戻って", "チャージして", "ホームベースへ", "充電が必要ですか？", "充電時間はどのくらいですか？")
                message.contains("YouTube") -> listOf("おすすめのYouTubeチャンネルは何ですか？", "YouTube動画の検索方法を教えてください", "YouTube動画のダウンロード方法を教えてください","おもしろいYouTube動画を教えてください","人気のあるYouTube動画を教えてください")
                message.contains("追") -> listOf("追いかけて", "着いてきなさい", "一緒にしましょう","追跡番号を教えてください","荷物の追跡方法を教えてください")
                else -> listOf("いいです", "お疲れ様でした", "ストップ","了解しました","分かりました")
            }
            else -> emptyList()
        }
        return allSuggestions.shuffled().take(numberOfSuggestions)
    }

}