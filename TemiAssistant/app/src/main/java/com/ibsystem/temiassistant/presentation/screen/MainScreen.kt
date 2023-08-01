package com.ibsystem.temiassistant.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ibsystem.temiassistant.navigation.Screen

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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { navController.navigate(Screen.ChatScreen.route) }) {
                Text("Chatbot")
            }
            Button(onClick = { navController.navigate(Screen.MapScreen.route) }) {
                Text("Map")
            }
            Button(onClick = { navController.navigate(Screen.MotionScreen.route) }) {
                Text("Motion")
            }
            Button(onClick = { navController.navigate(Screen.SettingsScreen.route) }) {
                Text("Settings")
            }
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
            modifier = Modifier.padding(16.dp)
        )
    }
}