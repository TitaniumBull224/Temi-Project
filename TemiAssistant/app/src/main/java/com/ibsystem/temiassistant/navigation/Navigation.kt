package com.ibsystem.temiassistant.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ibsystem.temiassistant.presentation.screen.MainScreen
import com.ibsystem.temiassistant.presentation.screen.chat.ChatScreen
import com.ibsystem.temiassistant.presentation.screen.chat.ChatScreenViewModel
import com.ibsystem.temiassistant.presentation.screen.map.MapScreen
import com.ibsystem.temiassistant.presentation.screen.map.MapScreenViewModel
import com.ibsystem.temiassistant.presentation.screen.settings.SettingsScreen

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun Navigation(navController: NavHostController, chatViewModel: ChatScreenViewModel, mapViewModel: MapScreenViewModel){
    NavHost(
        navController = navController,
        startDestination = Screen.MainScreen.route
    ) {
        composable(route = Screen.MainScreen.route) {
            MainScreen(navController = navController)
        }
        composable(route = Screen.ChatScreen.route) {
            ChatScreen(navController = navController, viewModel = chatViewModel)
        }
        composable(route = Screen.MapScreen.route) {
            MapScreen(navController = navController, viewModel = mapViewModel)
        }
//        composable(route = Screen.MotionScreen.route) {
//            MotionScreen(navController = navController, viewModel = mapViewModel)
//        }
        composable(route = Screen.SettingsScreen.route) {
            SettingsScreen(navController = navController)
        }
    }
}