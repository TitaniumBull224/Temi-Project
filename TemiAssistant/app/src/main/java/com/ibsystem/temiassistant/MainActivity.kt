package com.ibsystem.temiassistant

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
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ibsystem.temiassistant.navigation.Navigation
import com.ibsystem.temiassistant.presentation.screen.chat.ChatViewModel
import com.ibsystem.temiassistant.domain.model.MessageBody
import com.ibsystem.temiassistant.presentation.screen.map.MapViewModel
import com.ibsystem.temiassistant.presentation.screen.order_list.OrderViewModel
import com.ibsystem.temiassistant.presentation.screen.settings.SettingsViewModel
import com.ibsystem.temiassistant.ui.theme.ComposeUiTempletesTheme
import com.robotemi.sdk.Robot
import com.robotemi.sdk.listeners.OnConversationStatusChangedListener
import com.robotemi.sdk.listeners.OnDetectionDataChangedListener
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener
import com.robotemi.sdk.listeners.OnRobotReadyListener
import com.robotemi.sdk.listeners.OnUserInteractionChangedListener
import com.robotemi.sdk.map.OnLoadMapStatusChangedListener
import com.robotemi.sdk.model.DetectionData
import com.robotemi.sdk.navigation.listener.OnCurrentPositionChangedListener
import com.robotemi.sdk.navigation.model.Position
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
@Suppress("DEPRECATION")
class MainActivity : ComponentActivity(), OnRobotReadyListener, Robot.AsrListener,
    OnConversationStatusChangedListener, OnCurrentPositionChangedListener,
    OnDetectionStateChangedListener, OnDetectionDataChangedListener, OnUserInteractionChangedListener,
    OnLoadMapStatusChangedListener, OnGoToLocationStatusChangedListener {
    private val TAG = MainActivity::class.java.simpleName
    private val mRobot = Robot.getInstance() // Create mRobot before any of viewModel
    private lateinit var fusedLocationListener: FusedLocationProviderClient

    private val chatViewModel by viewModels<ChatViewModel>()
    private val mapViewModel by viewModels<MapViewModel>()
    private val orderViewModel by viewModels<OrderViewModel>()
    private val settingsViewModel = SettingsViewModel.getInstance()


    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationListener = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()

        setContent {
            ComposeUiTempletesTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    Navigation(navController, chatViewModel, mapViewModel, orderViewModel)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "Robot: OnStart")
        mRobot.addOnRobotReadyListener(this)
        mRobot.addAsrListener(this)
        mRobot.addOnConversationStatusChangedListener(this)
        mRobot.addOnCurrentPositionChangedListener(this)
        mRobot.addOnDetectionStateChangedListener(this)
        mRobot.addOnDetectionDataChangedListener(this)
        mRobot.addOnUserInteractionChangedListener(this)
        mRobot.addOnLoadMapStatusChangedListener(this)
        mRobot.addOnGoToLocationStatusChangedListener(this)
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "Robot: OnStop")
        mRobot.removeOnRobotReadyListener(this)
        mRobot.removeAsrListener(this)
        mRobot.removeOnConversationStatusChangedListener(this)
        mRobot.removeOnCurrentPositionChangedListener(this)
        mRobot.setDetectionModeOn(false, 2.0f) // Set detection mode off
        mRobot.trackUserOn = false // Set tracking mode off
        Log.i(TAG, "Set detection mode: OFF\nSet track user: OFF")
        mRobot.removeOnDetectionStateChangedListener(this)
        mRobot.removeOnDetectionDataChangedListener(this)
        mRobot.removeOnUserInteractionChangedListener(this)
        mRobot.removeOnLoadMapStatusChangedListener(this)
        mRobot.removeOnGoToLocationStatusChangedListener(this)
    }

    override fun onRobotReady(isReady: Boolean) {
        if (isReady) {
            Log.i(TAG, "Robot: OnRobotReady")
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
        Log.i(TAG, "ASR Result: $asrResult")
    }

    override fun onConversationStatusChanged(status: Int, text: String) {
        val statusStr = when (status) {
            OnConversationStatusChangedListener.IDLE -> "IDLE"
            OnConversationStatusChangedListener.LISTENING -> "LISTENING"
            OnConversationStatusChangedListener.THINKING -> "THINKING"
            OnConversationStatusChangedListener.SPEAKING -> "SPEAKING"
            else -> "UNKNOWN"
        }
        Log.i(TAG, "Status: $statusStr | Text: $text")
        if (statusStr == "LISTENING") {
            Toast.makeText(this, "受信中", Toast.LENGTH_LONG).show()
        }

    }

    override fun onCurrentPositionChanged(position: Position) {
        mapViewModel.setPosition(position)
        chatViewModel.setCurrentPosition(position)
//        val str = "Current Position: X: " + position.x + " Y: " + position.y
//        Log.i(TAG, str)
    }

    override fun onDetectionStateChanged(state: Int) {
        val stateStr = when (state) {
            OnDetectionStateChangedListener.IDLE -> "IDLE" // No active detection and/or 10 seconds have passed since the last detection was lost
            OnDetectionStateChangedListener.LOST -> "LOST" // When human-target is lost
            OnDetectionStateChangedListener.DETECTED -> "DETECTED" // Human is detected
            else -> "UNKNOWN" // This should not happen
        }
        Log.i(TAG, "Detection State: $stateStr")
        lifecycleScope.launch {
            if(stateStr == "DETECTED" && settingsViewModel.isDetectionOn.first()) {
                val greetings = listOf("こんにちは", "おはようございます", "こんばんは", "お元気ですか？", "どうして逃げるんだい？", "怪しいもんじゃないんだから")
                val randomGreeting = greetings.random()
                mRobot.askQuestion(randomGreeting)
                mRobot.beWithMe()
            }
        }
    }

    override fun onDetectionDataChanged(detectionData: DetectionData) {
        if (detectionData.isDetected) {
            Log.i(TAG, "Detection Data: " + detectionData.distance + " m")
        }
    }

    override fun onUserInteraction(isInteracting: Boolean) {
        val str = if (isInteracting) "ON" else "OFF"
        // User is interacting with the robot:
        // - User is detected
        // - User is interacting by touch, voice, or in telepresence-mode
        // - Robot is moving
        Log.i(TAG, "User Interaction: $str")
    }

    // MAP RELATED FUNCTION

    override fun onLoadMapStatusChanged(status: Int, requestId: String) {
        Log.i(TAG, "load map status: $status, requestId: $requestId")
    }

    override fun onGoToLocationStatusChanged(
        location: String,
        status: String,
        descriptionId: Int,
        description: String
    ) {
        if (status == OnGoToLocationStatusChangedListener.COMPLETE) {
            mRobot.askQuestion(if (location == HOME_BASE_LOCATION) "ただいま" else "お待たせしました！")
        }
    }

    companion object {
        const val HOME_BASE_LOCATION = "ホームベース"

        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    // GET PHYSICAL LOCATION
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
                        Log.i("LOCATION LON",location.longitude.toString())
                        Log.i("LOCATION LAT",location.latitude.toString())
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

    @Deprecated("Deprecated in Java")
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
