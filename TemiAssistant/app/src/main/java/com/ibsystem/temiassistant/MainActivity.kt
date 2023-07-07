package com.ibsystem.temiassistant

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ibsystem.temiassistant.databinding.ActivityMainBinding
import com.robotemi.sdk.NlpResult
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.listeners.OnConversationStatusChangedListener
import com.robotemi.sdk.listeners.OnDetectionDataChangedListener
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener
import com.robotemi.sdk.listeners.OnRobotReadyListener
import com.robotemi.sdk.listeners.OnUserInteractionChangedListener
import com.robotemi.sdk.model.DetectionData
import com.robotemi.sdk.navigation.listener.OnCurrentPositionChangedListener
import com.robotemi.sdk.navigation.model.Position
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.LinkedList
import java.util.Queue


class MainActivity : AppCompatActivity(), OnRobotReadyListener, Robot.AsrListener,
    OnConversationStatusChangedListener, OnCurrentPositionChangedListener,
    OnDetectionStateChangedListener, OnDetectionDataChangedListener, OnUserInteractionChangedListener,
    Robot.NlpListener {
    private val TAG = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding
    private lateinit var mRobot: Robot
    @SuppressLint("SetTextI18n")
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
                            mRobot.speak(
                                TtsRequest.create(
                                    queue.remove(),
                                    true
                                )
                            ) // do not display text; uses Google Text-to-Speech
                        }
                    }
                }
            })

            mRobot.speak(TtsRequest.create(queue.remove(), false))
        }

        // Initialize record button
        binding.recordBtn.setOnClickListener {
            mRobot.askQuestion("起動しなす")
        }


        // Move button
        binding.moveBtn.setOnClickListener {
            var xCoordinate: Float = 0.0F // Robot's position wrt the Home Base [m]
            var yCoordinate: Float = 0.0F // Robot's position wrt the Home Base [m]
            val yaw = 0 // Robot's yaw-rotation wrt the Home Base [deg]

            // Convert input string to integer
            try {
                xCoordinate = binding.posX.text.toString().toFloat()
            } catch (e: NumberFormatException) {
                e.printStackTrace();
            }

            try {
                yCoordinate = binding.posY.text.toString().toFloat()
            } catch (e: NumberFormatException) {
                e.printStackTrace();
            }

            Log.i(TAG, "X: $xCoordinate | Y: $yCoordinate");

            // Send robot to the XY position
            mRobot.goToPosition(
                Position(xCoordinate, yCoordinate, yaw.toFloat(), 0)
            );
        }

        // Detection button
        binding.detectionBtn.setOnClickListener {
            if (mRobot.detectionModeOn || mRobot.trackUserOn) {
                Log.i(TAG, "Set detection mode: OFF")
                mRobot.setDetectionModeOn(false, 2.0f) // Set detection mode off
                Log.i(TAG, "Set track user: OFF")
                mRobot.trackUserOn = false // Set tracking mode off
                binding.userInteraction.text = "User Interaction: OFF"
                binding.detectionData.text = "Detection Data: No DATA"
                // Note: When exiting the application, track user will still be enabled unless manually disabled
            } else {
                Log.i(TAG, "Set detection mode: ON")
                mRobot.setDetectionModeOn(true, 2.0f) // Set detection mode on; set detection distance to be 2.0 m
                Log.i(TAG, "Set track user: ON")
                mRobot.trackUserOn = true // Set tracking mode on
            }
        }

        binding.testNlu.setOnClickListener{
            mRobot.startDefaultNlu("ホームベースに行く")
            Log.i(TAG,"ホームベースに行く")
        }
    }

    override fun onStart() {
        super.onStart()
        // Add robot event listeners
        Log.i(TAG, "Robot: OnStart")
        mRobot.addOnRobotReadyListener(this)
        mRobot.addAsrListener(this)
        mRobot.addOnConversationStatusChangedListener(this)
        mRobot.addOnCurrentPositionChangedListener(this)

        mRobot.addOnDetectionStateChangedListener(this)
        mRobot.addOnDetectionDataChangedListener(this)
        mRobot.addOnUserInteractionChangedListener(this)

        mRobot.addNlpListener(this)

    }

    override fun onStop() {
        super.onStop()
        // Remove robot event listeners
        Log.i(TAG, "Robot: OnStop")
        mRobot.removeOnRobotReadyListener(this)
        mRobot.removeAsrListener(this)
        mRobot.removeOnConversationStatusChangedListener(this)
        mRobot.removeOnCurrentPositionChangedListener(this)

        Log.i(TAG, "Set detection mode: OFF")
        mRobot.setDetectionModeOn(false, 2.0f) // Set detection mode off
        Log.i(TAG, "Set track user: OFF")
        mRobot.trackUserOn = false // Set tracking mode off
        mRobot.removeOnDetectionStateChangedListener(this)
        mRobot.removeOnDetectionDataChangedListener(this)
        mRobot.removeOnUserInteractionChangedListener(this)

        mRobot.removeNlpListener(this)
    }

    override fun onRobotReady(isReady: Boolean) {
        if (isReady) {
            Log.i(TAG, "Robot: OnRobotReady")
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
        mRobot.finishConversation() // stop ASR listener
        Log.i(TAG, "ASR Result: $asrResult")
        binding.titleTxt.text = "ASR Result: $asrResult"
//        when {
//            asrResult.equals("こんにちは") -> {
//                mRobot.askQuestion("こんにちは、ご用件は?")
//            }
//            asrResult.contains("チャージ") -> {
//                mRobot.goTo("ホームベース")
//            }
//            else -> {
//                mRobot.askQuestion("すみませんが、適切なスキルが見つかりません")
//            }
//        }
        mRobot.startDefaultNlu(asrResult)
    }

    @SuppressLint("SetTextI18n")
    override fun onConversationStatusChanged(status: Int, text: String) {
        Log.i(TAG, "Status: START")

        lifecycleScope.launch(Dispatchers.Main) {
            val statusStr = when (status) {
                OnConversationStatusChangedListener.IDLE -> "IDLE"
                OnConversationStatusChangedListener.LISTENING -> "LISTENING"
                OnConversationStatusChangedListener.THINKING -> "THINKING"
                OnConversationStatusChangedListener.SPEAKING -> "SPEAKING"
                else -> "UNKNOWN"
            }
            Log.i(TAG, "Status: $statusStr | Text: $text")
            binding.statusTxt.text = "Status: $statusStr | Text: $text"
        }
    }

    override fun onCurrentPositionChanged(position: Position) {
        val str = "Current Position: X: " + position.x + " Y: " + position.y
        Log.i(TAG, str)
        binding.textViewPosition.text = str
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus
        if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) && view is EditText && !view.javaClass.name.startsWith(
                "android.webkit."
            )
        ) {
            val scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords)
            val x = ev.rawX + view.getLeft() - scrcoords[0]
            val y = ev.rawY + view.getTop() - scrcoords[1]
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) (this.getSystemService(
                INPUT_METHOD_SERVICE
            ) as InputMethodManager).hideSoftInputFromWindow(
                this.window.decorView.applicationWindowToken, 0
            )
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onDetectionStateChanged(state: Int) {
        val stateStr = when (state) {
            OnDetectionStateChangedListener.IDLE -> "IDLE" // No active detection and/or 10 seconds have passed since the last detection was lost
            OnDetectionStateChangedListener.LOST -> "LOST" // When human-target is lost
            OnDetectionStateChangedListener.DETECTED -> "DETECTED" // Human is detected
            else -> "UNKNOWN" // This should not happen
        }
        Log.i(TAG, "Detection State: $stateStr")
        binding.detectionState.text = "Detection State: $stateStr"
    }

    @SuppressLint("SetTextI18n")
    override fun onDetectionDataChanged(detectionData: DetectionData) {
        if (detectionData.isDetected) {
            binding.detectionData.text = "Detection Data: " + detectionData.distance + " m"
            Log.i(TAG, "Detection Data: " + detectionData.distance + " m")
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onUserInteraction(isInteracting: Boolean) {
        val str = if (isInteracting) "ON" else "OFF"
        // User is interacting with the robot:
        // - User is detected
        // - User is interacting by touch, voice, or in telepresence-mode
        // - Robot is moving
        Log.i(TAG, "User Interaction: $str")
        binding.userInteraction.text = "User Interaction: $str"
    }

    override fun onNlpCompleted(nlpResult: NlpResult) {
        Log.i(TAG, "onNlpCompleted")
        when (nlpResult.action) {
            "home.welcome" -> mRobot.tiltAngle(23)
            "home.dance" -> {
                val t = System.currentTimeMillis()
                val end = t + 5000
                while (System.currentTimeMillis() < end) {
                    mRobot.skidJoy(0f, 1f)
                }
            }
            "home.sleep" -> {
                Log.i(TAG, "Home Sweat Home baby!")
                mRobot.goTo("ホームベース")
            }
        }
    }


}
