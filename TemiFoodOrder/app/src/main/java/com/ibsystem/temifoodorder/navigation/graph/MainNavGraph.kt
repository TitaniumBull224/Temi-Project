package com.ibsystem.temifoodorder.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ibsystem.temifoodorder.navigation.screen.BottomNavItemScreen
import com.ibsystem.temifoodorder.navigation.screen.Screen
import com.ibsystem.temifoodorder.presentation.screen.about.AboutScreen
import com.ibsystem.temifoodorder.presentation.screen.cart.CartScreen
//import com.ibsystem.temifoodorder.presentation.screen.about.AboutScreen
//import com.ibsystem.temifoodorder.presentation.screen.cart.CartScreen
import com.ibsystem.temifoodorder.presentation.screen.detail.DetailScreen
import com.ibsystem.temifoodorder.presentation.screen.home.HomeScreen
//import com.ibsystem.temifoodorder.presentation.screen.explore.ExploreScreen
//import com.ibsystem.temifoodorder.presentation.screen.home.HomeScreen
import com.ibsystem.temifoodorder.presentation.screen.order.OrderScreen
import com.ibsystem.temifoodorder.presentation.screen.order.OrderViewModel
import com.ibsystem.temifoodorder.presentation.screen.search.SearchScreen
import com.ibsystem.temifoodorder.utils.Constants.PRODUCT_ARGUMENT_KEY

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

//        composable(route = BottomNavItemScreen.Home.route) {
//            OrderScreen()
//        }
//        composable(route = BottomNavItemScreen.Explore.route) {
//            ExploreScreen()
//        }
        composable(route = BottomNavItemScreen.Cart.route) {
            CartScreen()
        }
        composable(route = BottomNavItemScreen.About.route) {
            //AboutScreen()
            OrderScreen()
        }

        searchNavGraph()

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

fun NavGraphBuilder.searchNavGraph() {
    navigation(
        route = Graph.SEARCH,
        startDestination = Screen.Search.route
    ) {
        composable(route = Screen.Search.route) {
            SearchScreen()
        }
    }
}