package com.ibsystem.temiassistant.presentation.map_ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ibsystem.temiassistant.R
import com.ibsystem.temiassistant.presentation.chat_ui.TopBarSection
import com.robotemi.sdk.map.MapModel

@Composable
fun MapScreen(navController: NavController, viewModel: MapScreenViewModel) {
    val mapList = viewModel.mapList


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TopBarSection(
                username = "Chatbot",
                profile = painterResource(id = R.drawable.ic_final_icon),
                isOnline = true,
                onBack = { navController.navigateUp() }
            )


        }
    }
}

@Composable
fun MapList(mapList: List<MapModel>, onMapSelected: (MapModel) -> Unit) {
    LazyColumn {
        items(mapList) { map ->
            Text(
                text = map.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onMapSelected(map) }
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun MapListDialog(
    mapList: List<MapModel>,
    onMapSelected: (MapModel) -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Click item to load specific map") },
        text = { MapList(mapList, onMapSelected) },
        buttons = {
            Button(onClick = onDismissRequest) {
                Text("Close")
            }
        }
    )
}