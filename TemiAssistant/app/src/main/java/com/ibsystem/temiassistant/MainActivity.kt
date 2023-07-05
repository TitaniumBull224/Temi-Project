package com.ibsystem.temiassistant

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ibsystem.temiassistant.databinding.ActivityMainBinding
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.listeners.OnConversationStatusChangedListener
import com.robotemi.sdk.listeners.OnRobotReadyListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.LinkedList
import java.util.Queue
import org.jetbrains.annotations.NotNull


class MainActivity : AppCompatActivity(), OnRobotReadyListener, Robot.AsrListener,
    OnConversationStatusChangedListener  {
    private val TAG = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding
    private lateinit var mRobot: Robot
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize robot instance
        mRobot = Robot.getInstance()

        val queue: Queue<String> = LinkedList()

        // Initialize exit button
        binding.exitBtn.setOnClickListener { mRobot.showAppList() }

        // Initialize speak button
        binding.speakBtn.setOnClickListener {
            // Declare a queue of phrases
            queue.add("終末何をしてますか？忙しいですか？救ってもらってもいいんですか？")
            queue.add("終わりのお知らせを宣言する")

            // Register TTS listener
            mRobot.addTtsListener(object : Robot.TtsListener {
                override fun onTtsStatusChanged(ttsRequest: TtsRequest) {
                    Log.i(TAG, "Status:" + ttsRequest.status)
                    if (ttsRequest.status == TtsRequest.Status.COMPLETED) {
                        if (!queue.isEmpty()) {
                            mRobot.speak(TtsRequest.create(queue.remove(), false)) // do not display text; uses Google Text-to-Speech
                        }
                    }
                }
            })

            mRobot.speak(TtsRequest.create(queue.remove(), false))
        }

        // Initialize record button
        binding.recordBtn.setOnClickListener { mRobot.askQuestion("Hello") }
    }

    override fun onStart() {
        super.onStart()
        // Add robot event listeners
        mRobot.addOnRobotReadyListener(this)
        mRobot.addAsrListener(this)
        mRobot.addOnConversationStatusChangedListener(this)

    }

    override fun onStop() {
        super.onStop()
        // Remove robot event listeners
        mRobot.removeOnRobotReadyListener(this)
        mRobot.removeAsrListener(this)
        mRobot.removeOnConversationStatusChangedListener(this)
    }

    override fun onRobotReady(isReady: Boolean) {
        if (isReady) {
            Log.i(TAG, "Robot is ready")
            try {
                // Hide temi's ActivityBar and pull-down bar
                val activityInfo = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA)
                mRobot.onStart(activityInfo)
                mRobot.hideTopBar()
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onAsrResult(asrResult: String) {
        Log.i(TAG, "ASR Result: $asrResult")
        binding.titleTxt.text = "ASR Result: $asrResult"
        mRobot.finishConversation() // stop ASR listener
    }

    @SuppressLint("SetTextI18n")
    override fun onConversationStatusChanged(status: Int, text: String) {

        lifecycleScope.launch(Dispatchers.Main) {

            when (status) {
                OnConversationStatusChangedListener.IDLE -> {
                    Log.i(TAG, "Status: IDLE | Text: $text")
                    binding.statusTxt.text = "Status: IDLE | Text: $text"
                }

                OnConversationStatusChangedListener.LISTENING -> {
                    Log.i(TAG, "Status: LISTENING | Text: $text")
                    binding.statusTxt.text = "Status: LISTENING | Text: $text"
                }

                OnConversationStatusChangedListener.THINKING -> {
                    Log.i(TAG, "Status: THINKING | Text: $text")
                    binding.statusTxt.text = "Status: THINKING | Text: $text"
                }

                OnConversationStatusChangedListener.SPEAKING -> {
                    Log.i(TAG, "Status: SPEAKING | Text: $text")
                    binding.statusTxt.text = "Status: SPEAKING | Text: $text"
                }

                else -> {
                    Log.i(TAG, "Status: UNKNOWN | Text: $text")
                    binding.statusTxt.text = "Status: UNKNOWN | Text: $text"
                }
            }
        }
    }
}
