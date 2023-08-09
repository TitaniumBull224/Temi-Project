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

data class ScreenNames(
    val name: String,
    val route: String
)

val screens = listOf(
    ScreenNames("Chat Screen", Screen.ChatScreen.route)
)

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
//        LazyColumn {
//            items(screens){screen ->
//                Item(name = screen.name){
//                    navController.navigate(screen.route)
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//        }

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

            ButtonGradient(
                name = "Map",
                onClick = { navController.navigate(Screen.MapScreen.route) }
            )

            ButtonGradient(
                name = "Settings",
                onClick = { navController.navigate(Screen.SettingsScreen.route) }
            )
        }
    }
}


@ExperimentalMaterialApi
@Composable
fun Item(
    name: String,
    onclick: () -> Unit
){
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        onClick = onclick
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(DIMENS_16dp)
        )
    }
}

