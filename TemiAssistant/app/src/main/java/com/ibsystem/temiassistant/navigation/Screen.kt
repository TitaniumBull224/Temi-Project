package com.ibsystem.temiassistant.navigation

sealed class Screen(val route: String) {
    object MainScreen: Screen("Main_screen")
    object ChatScreen: Screen("Chat_screen")
    object MapScreen: Screen("Map_screen")
    object MotionScreen: Screen("Motion_screen")
    object SettingsScreen: Screen("Settings_screen")
    object OrderListScreen : Screen("order_list_screen")
    object CustomerScreen : Screen("customer_screen")
}