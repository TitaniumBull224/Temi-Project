package com.ibsystem.temiassistant.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ibsystem.temiassistant.R
import com.ibsystem.temiassistant.navigation.Screen
import com.ibsystem.temiassistant.presentation.common.component.GifImage
import com.ibsystem.temiassistant.presentation.common.component.ButtonGradient
import com.ibsystem.temiassistant.ui.theme.DIMENS_16dp
import com.ibsystem.temiassistant.ui.theme.DIMENS_32dp

@ExperimentalMaterialApi
@Composable
fun MainScreen(navController: NavController) {
    GifImage(
        modifier = Modifier.fillMaxSize(),
        gif = R.drawable.lantern
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(DIMENS_32dp, alignment = Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ButtonGradient(
                name = "Order",
                onClick = { navController.navigate(Screen.OrderListScreen.route) }
            )

            ButtonGradient(
                name = "Chatbot",
                onClick = { navController.navigate(Screen.ChatScreen.route) }
            )

//            ButtonGradient(
//                name = "Map",
//                onClick = { navController.navigate(Screen.MapScreen.route) }
//            )

            ButtonGradient(
                name = "Settings",
                onClick = { navController.navigate(Screen.SettingsScreen.route) }
            )
        }
    }
}

