package com.ibsystem.temifooddelivery.navigation.screen

sealed class Screen(val route: String) {
    object OrderListScreen : Screen("order_list_screen")
    object CustomerScreen : Screen("customer_screen")

}