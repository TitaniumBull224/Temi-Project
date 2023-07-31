package com.ibsystem.temifooddelivery

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TemiFoodDeliveryApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}