package com.ibsystem.temiassistant

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.annotation.CheckResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.ibsystem.temiassistant.databinding.ActivityMainBinding
import com.robotemi.sdk.NlpResult
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.constants.Page
import com.robotemi.sdk.listeners.OnConversationStatusChangedListener
import com.robotemi.sdk.listeners.OnDetectionDataChangedListener
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener
import com.robotemi.sdk.listeners.OnRobotReadyListener
import com.robotemi.sdk.listeners.OnUserInteractionChangedListener
import com.robotemi.sdk.map.MapModel
import com.robotemi.sdk.map.OnLoadMapStatusChangedListener
import com.robotemi.sdk.model.DetectionData
import com.robotemi.sdk.navigation.listener.OnCurrentPositionChangedListener
import com.robotemi.sdk.navigation.model.Position
import com.robotemi.sdk.permission.Permission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.LinkedList
import java.util.Queue


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), OnRobotReadyListener, Robot.AsrListener,
    OnConversationStatusChangedListener, OnCurrentPositionChangedListener,
    OnDetectionStateChangedListener, OnDetectionDataChangedListener, OnUserInteractionChangedListener,
    Robot.NlpListener, OnLoadMapStatusChangedListener {
    private val tag = MainActivity::class.java.simpleName
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
                    Log.i(tag, "Status:" + ttsRequest.status)
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
            var xCoordinate = 0.0F // Robot's position wrt the Home Base [m]
            var yCoordinate = 0.0F // Robot's position wrt the Home Base [m]
            val yaw = 0 // Robot's yaw-rotation wrt the Home Base [deg]

            // Convert input string to integer
            try {
                xCoordinate = binding.posX.text.toString().toFloat()
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }

            try {
                yCoordinate = binding.posY.text.toString().toFloat()
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }

            Log.i(tag, "X: $xCoordinate | Y: $yCoordinate")

            // Send robot to the XY position
            mRobot.goToPosition(
                Position(xCoordinate, yCoordinate, yaw.toFloat(), 0)
            )
        }

        // Detection button
        binding.detectionBtn.setOnClickListener {
            if (mRobot.detectionModeOn || mRobot.trackUserOn) {
                Log.i(tag, "Set detection mode: OFF")
                mRobot.setDetectionModeOn(false, 2.0f) // Set detection mode off
                Log.i(tag, "Set track user: OFF")
                mRobot.trackUserOn = false // Set tracking mode off
                binding.userInteraction.text = "User Interaction: OFF"
                binding.detectionData.text = "Detection Data: No DATA"
                // Note: When exiting the application, track user will still be enabled unless manually disabled
            } else {
                Log.i(tag, "Set detection mode: ON")
                mRobot.setDetectionModeOn(true, 2.0f) // Set detection mode on; set detection distance to be 2.0 m
                Log.i(tag, "Set track user: ON")
                mRobot.trackUserOn = true // Set tracking mode on
            }
        }

        binding.testNlu.setOnClickListener{
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        // Add robot event listeners
        Log.i(tag, "Robot: OnStart")
        mRobot.addOnRobotReadyListener(this)
        mRobot.addAsrListener(this)
        mRobot.addOnConversationStatusChangedListener(this)
        mRobot.addOnCurrentPositionChangedListener(this)

        mRobot.addOnDetectionStateChangedListener(this)
        mRobot.addOnDetectionDataChangedListener(this)
        mRobot.addOnUserInteractionChangedListener(this)

        mRobot.addNlpListener(this)
        mRobot.addOnLoadMapStatusChangedListener(this)

    }

    override fun onStop() {
        super.onStop()
        // Remove robot event listeners
        Log.i(tag, "Robot: OnStop")
        mRobot.removeOnRobotReadyListener(this)
        mRobot.removeAsrListener(this)
        mRobot.removeOnConversationStatusChangedListener(this)
        mRobot.removeOnCurrentPositionChangedListener(this)

        Log.i(tag, "Set detection mode: OFF")
        mRobot.setDetectionModeOn(false, 2.0f) // Set detection mode off
        Log.i(tag, "Set track user: OFF")
        mRobot.trackUserOn = false // Set tracking mode off
        mRobot.removeOnDetectionStateChangedListener(this)
        mRobot.removeOnDetectionDataChangedListener(this)
        mRobot.removeOnUserInteractionChangedListener(this)

        mRobot.removeNlpListener(this)
        mRobot.removeOnLoadMapStatusChangedListener(this)
    }

    override fun onRobotReady(isReady: Boolean) {
        if (isReady) {
            Log.i(tag, "Robot: OnRobotReady")
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
        Log.i(tag, "ASR Result: $asrResult")
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
        Log.i(tag, "Status: START")

        lifecycleScope.launch(Dispatchers.Main) {
            val statusStr = when (status) {
                OnConversationStatusChangedListener.IDLE -> "IDLE"
                OnConversationStatusChangedListener.LISTENING -> "LISTENING"
                OnConversationStatusChangedListener.THINKING -> "THINKING"
                OnConversationStatusChangedListener.SPEAKING -> "SPEAKING"
                else -> "UNKNOWN"
            }
            Log.i(tag, "Status: $statusStr | Text: $text")
            binding.statusTxt.text = "Status: $statusStr | Text: $text"
        }
    }

    override fun onCurrentPositionChanged(position: Position) {
        val str = "Current Position: X: " + position.x + " Y: " + position.y
        Log.i(tag, str)
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

    @SuppressLint("SetTextI18n")
    override fun onDetectionStateChanged(state: Int) {
        val stateStr = when (state) {
            OnDetectionStateChangedListener.IDLE -> "IDLE" // No active detection and/or 10 seconds have passed since the last detection was lost
            OnDetectionStateChangedListener.LOST -> "LOST" // When human-target is lost
            OnDetectionStateChangedListener.DETECTED -> "DETECTED" // Human is detected
            else -> "UNKNOWN" // This should not happen
        }
        Log.i(tag, "Detection State: $stateStr")
        binding.detectionState.text = "Detection State: $stateStr"
    }

    @SuppressLint("SetTextI18n")
    override fun onDetectionDataChanged(detectionData: DetectionData) {
        if (detectionData.isDetected) {
            binding.detectionData.text = "Detection Data: " + detectionData.distance + " m"
            Log.i(tag, "Detection Data: " + detectionData.distance + " m")
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onUserInteraction(isInteracting: Boolean) {
        val str = if (isInteracting) "ON" else "OFF"
        // User is interacting with the robot:
        // - User is detected
        // - User is interacting by touch, voice, or in telepresence-mode
        // - Robot is moving
        Log.i(tag, "User Interaction: $str")
        binding.userInteraction.text = "User Interaction: $str"
    }

    override fun onNlpCompleted(nlpResult: NlpResult) {
        Log.i(tag, "onNlpCompleted")
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
                Log.i(tag, "Home Sweat Home baby!")
                mRobot.goTo("ホームベース")
            }
        }
    }

    // MAP RELATED FUNCTION

    override fun onLoadMapStatusChanged(status: Int, requestId: String) {
        Log.i(tag, "load map status: $status, requestId: $requestId")
    }

    private var mapList: List<MapModel> = ArrayList()
    private fun getMapList() {
        if (requestPermissionIfNeeded(Permission.MAP, REQUEST_CODE_GET_MAP_LIST)) {
            return
        }
        mapList = mRobot.getMapList()
    }

    private fun loadMap(
        reposeRequired: Boolean,
        position: Position?,
        offline: Boolean = false,
        withoutUI: Boolean = false
    ) {
        if (mapList.isEmpty()) {
            getMapList()
        }
        if (mRobot.checkSelfPermission(Permission.MAP) != Permission.GRANTED) {
            return
        }
        val mapListString: MutableList<String> = ArrayList()
        for (i in mapList.indices) {
            mapListString.add(mapList[i].name)
        }
        val mapListAdapter = ArrayAdapter(this, R.layout.item_dialog_row, R.id.name, mapListString)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Click item to load specific map")
        builder.setAdapter(mapListAdapter, null)
        val dialog = builder.create()
        dialog.listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, pos: Int, _ ->
                val requestId =
                    mRobot.loadMap(
                        mapList[pos].id,
                        reposeRequired,
                        position,
                        offline = offline,
                        withoutUI = withoutUI
                    )
                Log.i(tag, "Loading map: ${mapList[pos]}, request id $requestId, reposeRequired $reposeRequired, position $position, offline $offline, withoutUI $withoutUI")
                dialog.dismiss()
            }
        dialog.show()
    }

    private fun loadMapToCache() {
        if (mapList.isEmpty()) {
            getMapList()
        }
        if (mRobot.checkSelfPermission(Permission.MAP) != Permission.GRANTED) {
            return
        }
        val mapListString: MutableList<String> = ArrayList()
        for (i in mapList.indices) {
            mapListString.add(mapList[i].name)
        }
        val mapListAdapter = ArrayAdapter(this, R.layout.item_dialog_row, R.id.name, mapListString)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Click item to load specific map")
        builder.setAdapter(mapListAdapter, null)
        val dialog = builder.create()
        dialog.listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, pos: Int, _ ->
                val requestId = mRobot.loadMapToCache(mapList[pos].id)
                Log.i(tag, "Loading map to cache: " + mapList[pos] + " request id " + requestId)
                dialog.dismiss()
            }
        dialog.show()
    }

    private fun getMapListBtn() {
        getMapList()
        for (mapModel in mapList) {
            Log.i(tag, "Map: $mapModel")
        }
    }

    private fun loadMap() {
        loadMap(false, null)
    }

    // PERMISSION CHECK
    @CheckResult
    private fun requestPermissionIfNeeded(permission: Permission, requestCode: Int): Boolean {
        if (mRobot.checkSelfPermission(permission) == Permission.GRANTED) {
            return false
        }
        mRobot.requestPermissions(listOf(permission), requestCode)
        return true
    }

    companion object {
        const val ACTION_HOME_WELCOME = "home.welcome"
        const val ACTION_HOME_DANCE = "home.dance"
        const val ACTION_HOME_SLEEP = "home.sleep"
        const val HOME_BASE_LOCATION = "home base"

        // Storage Permissions
        private const val REQUEST_EXTERNAL_STORAGE = 1
        private const val REQUEST_CODE_NORMAL = 0
        private const val REQUEST_CODE_FACE_START = 1
        private const val REQUEST_CODE_FACE_STOP = 2
        private const val REQUEST_CODE_MAP = 3
        private const val REQUEST_CODE_SEQUENCE_FETCH_ALL = 4
        private const val REQUEST_CODE_SEQUENCE_PLAY = 5
        private const val REQUEST_CODE_START_DETECTION_WITH_DISTANCE = 6
        private const val REQUEST_CODE_SEQUENCE_PLAY_WITHOUT_PLAYER = 7
        private const val REQUEST_CODE_GET_MAP_LIST = 8
        private const val REQUEST_CODE_GET_ALL_FLOORS = 9
        private val PERMISSIONS_STORAGE = arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        /**
         * Checks if the app has permission to write to device storage
         * If the app does not has permission then the user will be prompted to grant permissions
         */
        fun verifyStoragePermissions(activity: Activity?) {
            // Check if we have write permission
            val permission = ActivityCompat.checkSelfPermission(
                activity!!,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE

            )
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE)
            }
        }
    }



}
