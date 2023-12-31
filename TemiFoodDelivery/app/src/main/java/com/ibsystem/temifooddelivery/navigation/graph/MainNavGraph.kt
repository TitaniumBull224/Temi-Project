package com.ibsystem.temifooddelivery.navigation.graph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ibsystem.temifooddelivery.navigation.screen.Screen
import com.ibsystem.temifooddelivery.presentation.screen.customer.CustomerScreen
import com.ibsystem.temifooddelivery.presentation.screen.order_list.OrderListScreen
import com.ibsystem.temifooddelivery.presentation.screen.order_list.OrderViewModel

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun MainNavGraph(navController: NavHostController, viewModel: OrderViewModel) {
    NavHost(
        navController = navController,
        startDestination = Screen.OrderListScreen.route
    ) {
        composable(route = Screen.OrderListScreen.route) {
            OrderListScreen(navController = navController, viewModel = viewModel)
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
                viewModel = viewModel
            )
        }

        //detailsNavGraph()
    }
}

//fun NavGraphBuilder.detailsNavGraph() {
//    navigation(
//        route = Graph.DETAILS,
//        startDestination = Screen.CustomerScreen.route + "/{orderID}"
//    ) {
//        composable(
//            route = Screen.CustomerScreen.route,
//            arguments = listOf(navArgument(PRODUCT_ARGUMENT_KEY) {
//                type = NavType.
//            })
//        ) {
//            DetailScreen()
//        }
//    }
//}

