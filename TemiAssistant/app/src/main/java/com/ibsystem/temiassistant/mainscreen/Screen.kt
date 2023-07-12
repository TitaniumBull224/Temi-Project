package com.ibsystem.temiassistant.mainscreen

sealed class Screen(val route: String) {
    object MainScreen: Screen("Main_screen")
    object ChatScreen: Screen("Chat_screen")
}