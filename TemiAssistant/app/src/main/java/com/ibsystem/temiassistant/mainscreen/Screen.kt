package com.ibsystem.temiassistant.mainscreen

sealed class Screen(val route: String) {
    object MainScreen: Screen("Main_screen")
    object ChatScreen: Screen("Chat_screen")
    object MapScreen: Screen("Map_screen")
    object MotionScreen: Screen("Motion_screen")
    object SettingsScreen: Screen("Settings_screen")
}