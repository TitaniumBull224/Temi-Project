package com.ibsystem.temifooddelivery

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.ibsystem.temifooddelivery.presentation.screen.MainScreen
import com.ibsystem.temifooddelivery.presentation.screen.order_list.OrderViewModel
import com.ibsystem.temifooddelivery.ui.theme.GrayBackground
import com.ibsystem.temifooddelivery.ui.theme.TemiFoodDeliveryTheme
import com.robotemi.sdk.Robot
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener
import com.robotemi.sdk.listeners.OnRobotReadyListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Suppress("DEPRECATION")
class MainActivity : ComponentActivity(), OnRobotReadyListener, OnGoToLocationStatusChangedListener {
    private val TAG = MainActivity::class.java.simpleName
    private val mRobot = Robot.getInstance()
    private val orderViewModel by viewModels<OrderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TemiFoodDeliveryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = GrayBackground
                ) {
                    MainScreen(orderViewModel = orderViewModel)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "Robot: OnStart")
        mRobot.addOnRobotReadyListener(this)
        mRobot.addOnGoToLocationStatusChangedListener(this)
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "Robot: OnStop")
        mRobot.removeOnRobotReadyListener(this)
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

    override fun onGoToLocationStatusChanged(
        location: String,
        status: String,
        descriptionId: Int,
        description: String
    ) {
        if (status == OnGoToLocationStatusChangedListener.COMPLETE) {
            mRobot.askQuestion(if (location == "ホームベース") "ただいま" else "お待たせしました！")
        }
    }


//    private fun observeData() {
//        lifecycleScope.launch {
//            orderViewModel.uiState.collect {
//                    data -> when(data){
//                is ApiResult.Error -> {
//                    Log.e("Main Act", "Error: ${data.message}")
//                }
//                ApiResult.Loading -> {
//                    Log.i("Main Act", "IS LOADING")
//                }
//                is ApiResult.Success -> {
//                    val orders = data.data as List<*>
//                    orders.forEach{
//                        Log.i("Main Act", it.toString())
//                    }
//                }
//            }
//            }
//        }
//    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TemiFoodDeliveryTheme {
        Greeting("Android")
    }
}