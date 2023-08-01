package com.ibsystem.temiassistant.presentation.screen.map

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ibsystem.temiassistant.R
import com.ibsystem.temiassistant.presentation.component.TopBarSection
import com.ibsystem.temiassistant.ui.theme.Crimson
import com.robotemi.sdk.navigation.model.Position
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
                username = "Map",
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
                viewModel.getPosition()?.let { position ->
                    ZoomableImage(bitmap, position)
                }
            }
        }

        if (mapDataModel != null) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Text("Map ID: ${mapDataModel.mapId}")
                Text("Map Info: ${mapDataModel.mapInfo}")
                Text("Green Paths: ${mapDataModel.greenPaths}")
                Text("Virtual Walls: ${mapDataModel.virtualWalls}")
                viewModel.getPosition()?.let { position ->
                    RobotPosition(position)
                }
            }
        }
    }
}

@Composable
fun ZoomableImage(bitmap: Bitmap, position: Position) {
    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
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
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = rotation,
                    translationX = offsetX,
                    translationY = offsetY
                )
        ) {
            drawImage(bitmap.asImageBitmap())
            drawCircle(
                color = Crimson,
                center = Offset(
                    (position.x * scale + offsetX),
                    (position.y * scale + offsetY)
                ),
                radius = 4.dp.toPx()
            )
        }
    }
}

@Composable
fun RobotPosition(position: Position) {
    Column {
        Text("Locations: ")
        Text("X: ${position.x}")
        Text("Y: ${position.y}")
    }
}