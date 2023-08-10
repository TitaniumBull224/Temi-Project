package com.ibsystem.temifoodorder.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ibsystem.temifoodorder.navigation.graph.MainNavGraph
import com.ibsystem.temifoodorder.presentation.component.BottomBar
import com.ibsystem.temifoodorder.presentation.screen.cart.CartViewModel
import com.ibsystem.temifoodorder.presentation.screen.home.HomeViewModel
import com.ibsystem.temifoodorder.presentation.screen.order.OrderViewModel
import com.ibsystem.temifoodorder.ui.theme.DIMENS_16dp
import com.ibsystem.temifoodorder.ui.theme.DIMENS_32dp

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),

) {
    Scaffold(
        bottomBar = {
            Surface(
                elevation = DIMENS_32dp,
                shape = RoundedCornerShape(topStart = DIMENS_16dp, topEnd = DIMENS_16dp)
            ) {
                BottomBar(navController = navController)
            }
        },
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            MainNavGraph(navController = navController)
        }
    }

}