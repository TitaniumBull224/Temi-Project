package com.ibsystem.temifooddelivery.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ibsystem.temifooddelivery.navigation.screen.BottomNavItemScreen
import com.ibsystem.temifooddelivery.navigation.screen.Screen
import com.ibsystem.temifooddelivery.presentation.screen.detail.DetailScreen
import com.ibsystem.temifooddelivery.presentation.screen.home.HomeScreen
import com.ibsystem.temifooddelivery.utils.Constants.PRODUCT_ARGUMENT_KEY

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = BottomNavItemScreen.Home.route
    ) {
        composable(route = BottomNavItemScreen.Home.route) {
            HomeScreen(navController = navController)
        }

        detailsNavGraph()
    }
}

fun NavGraphBuilder.detailsNavGraph() {
    navigation(
        route = Graph.DETAILS,
        startDestination = Screen.Details.route
    ) {
        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument(PRODUCT_ARGUMENT_KEY) {
                type = NavType.IntType
            })
        ) {
            DetailScreen()
        }
    }
}

