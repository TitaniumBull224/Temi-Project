package com.ibsystem.temifoodorder.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ibsystem.temifoodorder.navigation.screen.Screen
import com.ibsystem.temifoodorder.presentation.screen.MainScreen
//import com.ibsystem.temifoodorder.presentation.screen.splash.SplashScreen

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Screen.Splash.route
    ) {
//        composable(route = Screen.Splash.route) {
//            SplashScreen(navController = navController)
//        }

//        composable(route = Screen.OnBoarding.route) {
//            OnBoardingScreen(navController = navController)
//        }

        composable(route = Graph.MAIN) {
            MainScreen()
        }
    }
}