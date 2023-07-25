package com.ibsystem.temiassistant.mainscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ibsystem.temiassistant.presentation.chat_ui.ChatScreen
import com.ibsystem.temiassistant.presentation.chat_ui.ChatScreenViewModel
import com.ibsystem.temiassistant.presentation.map_ui.MapScreen
import com.ibsystem.temiassistant.presentation.map_ui.MapScreenViewModel
import com.ibsystem.temiassistant.presentation.motion_ui.MotionScreen
import com.ibsystem.temiassistant.presentation.setting_ui.SettingsScreen

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
        composable(route = Screen.MotionScreen.route) {
            MotionScreen(navController = navController, viewModel = mapViewModel)
        }
        composable(route = Screen.SettingsScreen.route) {
            SettingsScreen(navController = navController)
        }
    }
}