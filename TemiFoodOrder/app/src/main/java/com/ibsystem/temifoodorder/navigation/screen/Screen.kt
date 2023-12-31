package com.ibsystem.temifoodorder.navigation.screen

sealed class Screen(val route: String) {

    object Splash : Screen("splash_screen")

    object OnBoarding : Screen("on_boarding_screen")

    object Search : Screen("search_screen")

    object Details : Screen("details_screen/{productId}") {
        fun passProductId(productId: String): String = "details_screen/$productId"
    }

}