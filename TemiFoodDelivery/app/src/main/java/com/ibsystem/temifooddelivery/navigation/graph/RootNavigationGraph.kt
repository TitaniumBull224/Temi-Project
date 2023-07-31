package com.ibsystem.temifooddelivery.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ibsystem.temifooddelivery.navigation.screen.Screen
import com.ibsystem.temifooddelivery.presentation.screen.MainScreen

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Graph.MAIN) {
            MainScreen()
        }
    }
}