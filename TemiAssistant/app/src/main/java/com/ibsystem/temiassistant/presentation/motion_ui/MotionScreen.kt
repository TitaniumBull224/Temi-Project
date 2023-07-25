package com.ibsystem.temiassistant.presentation.motion_ui

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ibsystem.temiassistant.R
import com.ibsystem.temiassistant.presentation.chat_ui.ChatScreenViewModel
import com.ibsystem.temiassistant.presentation.chat_ui.TopBarSection
import com.ibsystem.temiassistant.presentation.map_ui.MapScreenViewModel
import com.ibsystem.temiassistant.presentation.map_ui.RobotPosition
import com.ibsystem.temiassistant.presentation.map_ui.ZoomableImage
import com.ibsystem.temiassistant.ui.theme.Red
import kotlin.math.roundToInt

@Composable
fun MotionScreen(navController: NavController, viewModel: MapScreenViewModel) {
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

            Canvas(modifier = Modifier.fillMaxSize()) {
                drawIntoCanvas { canvas ->
                    // Draw map using mapDataModel
                    if (mapDataModel != null) {
                        // Convert map data to a format that can be drawn on the canvas
                        val mapImage = createMapImage(mapDataModel)
                        // Draw the map image on the canvas
                        canvas.drawImage(mapImage, Offset.Zero)
                    }

                    // Draw robot position using currentPosition
                    if (currentPosition != null) {
                        // Convert robot position to canvas coordinates
                        val positionOnCanvas = convertPositionToCanvasCoordinates(currentPosition)
                        // Draw a circle to represent the robot's position
                        canvas.drawCircle(positionOnCanvas, radius = 10f, paint = Paint().apply {color = Red})
                    }
                }
            }




        }

    }
}