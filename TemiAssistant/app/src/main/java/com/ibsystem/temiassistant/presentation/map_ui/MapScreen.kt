package com.ibsystem.temiassistant.presentation.map_ui

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ibsystem.temiassistant.R
import com.ibsystem.temiassistant.presentation.chat_ui.TopBarSection
import com.robotemi.sdk.Robot
import com.robotemi.sdk.map.MapModel
import kotlin.math.roundToInt

@Composable
fun MapScreen(navController: NavController, viewModel: MapScreenViewModel) {
    viewModel.loadMapData()
    val mapDataModel = viewModel.mapDataModel

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

            if (mapDataModel != null) {
                val bitmap = remember {
                    Bitmap.createBitmap(
                        mapDataModel.mapImage.data.map { Color.argb((it * 2.55).roundToInt(), 0, 0, 0) }.toIntArray(),
                        mapDataModel.mapImage.cols,
                        mapDataModel.mapImage.rows,
                        Bitmap.Config.ARGB_8888
                    )
                }

//                Image(
//                    bitmap = bitmap.asImageBitmap(),
//                    contentDescription = "Map image",
//                    modifier = Modifier.fillMaxWidth()
//                )
                ZoomableImage(bitmap)
                Text("Map ID: ${mapDataModel.mapId}")
                Text("Map Info: ${mapDataModel.mapInfo}")
                Text("Green Paths: ${mapDataModel.greenPaths}")
                Text("Virtual Walls: ${mapDataModel.virtualWalls}")
                Text("Locations: ${mapDataModel.locations}")
            }
        }
    }
}

@Composable
fun ZoomableImage(bitmap: Bitmap) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val state = rememberTransformableState { zoomChange, _, _ ->
        scale *= zoomChange
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .transformable(state = state)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consumeAllChanges()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Map image",
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY
                )
        )
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