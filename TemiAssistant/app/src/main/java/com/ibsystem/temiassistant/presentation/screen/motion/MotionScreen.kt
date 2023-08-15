package com.ibsystem.temiassistant.presentation.screen.motion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.ibsystem.temiassistant.R
import com.ibsystem.temiassistant.presentation.common.component.TopBarSection
import com.ibsystem.temiassistant.presentation.screen.map.MapViewModel

@Composable
fun MotionScreen(navController: NavController, viewModel: MapViewModel) {
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
                onBack = { navController.navigateUp() }
            )

        }

    }
}