package com.ibsystem.temiassistant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ibsystem.temiassistant.databinding.ActivityMainBinding
import com.robotemi.sdk.Robot
import com.robotemi.sdk.listeners.OnRobotReadyListener

class MainActivity : AppCompatActivity(), OnRobotReadyListener {
    private lateinit var mRobot: Robot
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize robot instance
        mRobot = Robot.getInstance();

        // binding = DataBindingUtil.inflate(inflater, R.layout.activity_main , container, false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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