package com.ibsystem.temifooddelivery.presentation.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ibsystem.temifooddelivery.navigation.graph.MainNavGraph
import com.ibsystem.temifooddelivery.presentation.screen.order_list.OrderViewModel
import com.ibsystem.temifooddelivery.ui.theme.DIMENS_16dp
import com.ibsystem.temifooddelivery.ui.theme.DIMENS_32dp

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    orderViewModel: OrderViewModel
) {
    MainNavGraph(navController = navController, viewModel = orderViewModel)

}