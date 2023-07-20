package com.ibsystem.temiassistant

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.CheckResult
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ibsystem.temiassistant.mainscreen.Navigation
import com.ibsystem.temiassistant.presentation.chat_ui.ChatScreenViewModel
import com.ibsystem.temiassistant.presentation.chat_ui.MessageBody
import com.ibsystem.temiassistant.presentation.map_ui.MapScreenViewModel
import com.ibsystem.temiassistant.ui.theme.ComposeUiTempletesTheme
import com.robotemi.sdk.Robot
import com.robotemi.sdk.listeners.OnConversationStatusChangedListener
import com.robotemi.sdk.listeners.OnDetectionDataChangedListener
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener
import com.robotemi.sdk.listeners.OnRobotReadyListener
import com.robotemi.sdk.listeners.OnUserInteractionChangedListener
import com.robotemi.sdk.map.OnLoadMapStatusChangedListener
import com.robotemi.sdk.model.DetectionData
import com.robotemi.sdk.navigation.listener.OnCurrentPositionChangedListener
import com.robotemi.sdk.navigation.model.Position
import com.robotemi.sdk.permission.Permission


@Suppress("DEPRECATION")
class MainActivity : ComponentActivity(), OnRobotReadyListener, Robot.AsrListener, // Robot.NlpListener,
    OnConversationStatusChangedListener, OnCurrentPositionChangedListener,
    OnDetectionStateChangedListener, OnDetectionDataChangedListener, OnUserInteractionChangedListener,
    OnLoadMapStatusChangedListener {
    private val tag = MainActivity::class.java.simpleName
    lateinit var mRobot: Robot
    lateinit var fusedLocationListener: FusedLocationProviderClient
    lateinit var chatViewModel: ChatScreenViewModel
    lateinit var mapViewModel: MapScreenViewModel

    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRobot = Robot.getInstance()
        fusedLocationListener = LocationServices.getFusedLocationProviderClient(this)
        chatViewModel = ChatScreenViewModel(mRobot)
        setContent {
            ComposeUiTempletesTheme() {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    Navigation(navController, chatViewModel, mapViewModel)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(tag, "Robot: OnStart")
        mRobot.addOnRobotReadyListener(this)
        mRobot.addAsrListener(this)
        mRobot.addOnConversationStatusChangedListener(this)
        mRobot.addOnCurrentPositionChangedListener(this)
        mRobot.addOnDetectionStateChangedListener(this)
        mRobot.addOnDetectionDataChangedListener(this)
        mRobot.addOnUserInteractionChangedListener(this)

        mRobot.addOnLoadMapStatusChangedListener(this)
    }

    override fun onStop() {
        super.onStop()
        Log.i(tag, "Robot: OnStop")
        mRobot.removeOnRobotReadyListener(this)
        mRobot.removeAsrListener(this)
        mRobot.removeOnConversationStatusChangedListener(this)
        mRobot.removeOnCurrentPositionChangedListener(this)

        mRobot.setDetectionModeOn(false, 2.0f) // Set detection mode off
        mRobot.trackUserOn = false // Set tracking mode off
        Log.i(tag, "Set detection mode: OFF\nSet track user: OFF")
        mRobot.removeOnDetectionStateChangedListener(this)
        mRobot.removeOnDetectionDataChangedListener(this)
        mRobot.removeOnUserInteractionChangedListener(this)

        mRobot.removeOnLoadMapStatusChangedListener(this)
    }

    override fun onRobotReady(isReady: Boolean) {
        if (isReady) {
            Log.i(tag, "Robot: OnRobotReady")
            try {
                val activityInfo = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA)
                mRobot.onStart(activityInfo)
                mRobot.hideTopBar()
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    override fun onAsrResult(asrResult: String) {
        mRobot.finishConversation() // stop ASR listener
        chatViewModel.messageToWit(MessageBody(asrResult))
        Log.i(tag, "ASR Result: $asrResult")
        //　mRobot.startDefaultNlu(asrResult)
    }

    override fun onConversationStatusChanged(status: Int, text: String) {
        val statusStr = when (status) {
            OnConversationStatusChangedListener.IDLE -> "IDLE"
            OnConversationStatusChangedListener.LISTENING -> "LISTENING"
            OnConversationStatusChangedListener.THINKING -> "THINKING"
            OnConversationStatusChangedListener.SPEAKING -> "SPEAKING"
            else -> "UNKNOWN"
        }
        Log.i(tag, "Status: $statusStr | Text: $text")
        if (statusStr == "LISTENING") {
            Toast.makeText(this, "受信中", Toast.LENGTH_LONG).show()
        }

    }

    override fun onCurrentPositionChanged(position: Position) {
        val str = "Current Position: X: " + position.x + " Y: " + position.y
        Log.i(tag, str)
    }

    override fun onDetectionStateChanged(state: Int) {
        val stateStr = when (state) {
            OnDetectionStateChangedListener.IDLE -> "IDLE" // No active detection and/or 10 seconds have passed since the last detection was lost
            OnDetectionStateChangedListener.LOST -> "LOST" // When human-target is lost
            OnDetectionStateChangedListener.DETECTED -> "DETECTED" // Human is detected
            else -> "UNKNOWN" // This should not happen
        }
        Log.i(tag, "Detection State: $stateStr")
    }

    override fun onDetectionDataChanged(detectionData: DetectionData) {
        if (detectionData.isDetected) {
            Log.i(tag, "Detection Data: " + detectionData.distance + " m")
        }
    }

    override fun onUserInteraction(isInteracting: Boolean) {
        val str = if (isInteracting) "ON" else "OFF"
        // User is interacting with the robot:
        // - User is detected
        // - User is interacting by touch, voice, or in telepresence-mode
        // - Robot is moving
        Log.i(tag, "User Interaction: $str")
    }

    // MAP RELATED FUNCTION

    override fun onLoadMapStatusChanged(status: Int, requestId: String) {
        Log.i(tag, "load map status: $status, requestId: $requestId")
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
//        const val ACTION_HOME_WELCOME = "home.welcome"
//        const val ACTION_HOME_DANCE = "home.dance"
//        const val ACTION_HOME_SLEEP = "home.sleep"
//        const val HOME_BASE_LOCATION = "home base"

        // Storage Permissions
        private const val REQUEST_EXTERNAL_STORAGE = 1
//        private const val REQUEST_CODE_NORMAL = 0
//        private const val REQUEST_CODE_FACE_START = 1
//        private const val REQUEST_CODE_FACE_STOP = 2
//        private const val REQUEST_CODE_MAP = 3
//        private const val REQUEST_CODE_SEQUENCE_FETCH_ALL = 4
//        private const val REQUEST_CODE_SEQUENCE_PLAY = 5
//        private const val REQUEST_CODE_START_DETECTION_WITH_DISTANCE = 6
//        private const val REQUEST_CODE_SEQUENCE_PLAY_WITHOUT_PLAYER = 7
//        private const val REQUEST_CODE_GET_MAP_LIST = 8
//        private const val REQUEST_CODE_GET_ALL_FLOORS = 9
        private val PERMISSIONS_STORAGE = arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100

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

    private fun getCurrentLocation() {
        if(checkPermission()) {
            if(isLocationEnabled()) {
                fusedLocationListener.lastLocation.addOnCompleteListener(this) {task ->
                    val location: Location?=task.result
                    if(location == null) {
                        Toast.makeText(this, "Null Received", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this, "Get success", Toast.LENGTH_SHORT).show()
                        chatViewModel.longitude = location.longitude.toString()
                        chatViewModel.latitude = location.latitude.toString()
                    }
                }
            }
            else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
        else {
                requestPermission()
        }
    }

    private fun checkPermission(): Boolean {
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_ACCESS_LOCATION)
    }


    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(applicationContext, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
