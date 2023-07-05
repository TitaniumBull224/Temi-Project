package com.ibsystem.temiassistant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.robotemi.sdk.Robot
import com.robotemi.sdk.listeners.OnRobotReadyListener

class MainActivity : AppCompatActivity(), OnRobotReadyListener {
    private lateinit var mRobot: Robot
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize robot instance
        mRobot = Robot.getInstance();

    }

    override fun onStart() {
        super.onStart()

        // Add robot event listeners
        mRobot.addOnRobotReadyListener(this)
    }

    override fun onStop() {
        super.onStop()

        // Remove robot event listeners
        mRobot.removeOnRobotReadyListener(this)
    }

    override fun onRobotReady(isReady: Boolean) {
        if (isReady) {
            mRobot.hideTopBar() // hide temi's top action bar when skill is active
        }
    }
}