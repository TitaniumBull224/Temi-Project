package com.ibsystem.temiassistant.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ibsystem.temiassistant.presentation.screen.MainScreen
import com.ibsystem.temiassistant.presentation.screen.chat.ChatScreen
import com.ibsystem.temiassistant.presentation.screen.chat.ChatViewModel
import com.ibsystem.temiassistant.presentation.screen.map.MapScreen
import com.ibsystem.temiassistant.presentation.screen.map.MapViewModel
import com.ibsystem.temiassistant.presentation.screen.settings.SettingsScreen
import com.ibsystem.temiassistant.presentation.screen.customer.CustomerScreen
import com.ibsystem.temiassistant.presentation.screen.order_list.OrderListScreen
import com.ibsystem.temiassistant.presentation.screen.order_list.OrderViewModel

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun Navigation(navController: NavHostController, chatViewModel: ChatViewModel, mapViewModel: MapViewModel, orderViewModel: OrderViewModel){
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

        composable(route = Screen.OrderListScreen.route) {
            OrderListScreen(navController = navController, viewModel = orderViewModel)
        }
        composable(
            route = Screen.CustomerScreen.route + "/{orderID}",
            arguments = listOf(
                navArgument("orderID") {
                    nullable = true
                    type = NavType.StringType
                }
            )
        ) { entry ->
            CustomerScreen(
                navController = navController,
                orderID = entry.arguments?.getString("orderID")!!,
                viewModel = orderViewModel
            )
        }
    }
}