@file:OptIn(ExperimentalMaterialApi::class)

package com.ibsystem.temiassistant.presentation.screen.settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.ibsystem.temiassistant.R
import com.ibsystem.temiassistant.presentation.common.component.GifImage
import com.ibsystem.temiassistant.presentation.common.component.TextGradient
import com.ibsystem.temiassistant.presentation.common.component.TopBarSection
import com.ibsystem.temiassistant.ui.theme.Blue
import com.ibsystem.temiassistant.ui.theme.Chocolate
import com.ibsystem.temiassistant.ui.theme.DIMENS_16dp
import com.ibsystem.temiassistant.ui.theme.DIMENS_1dp
import com.ibsystem.temiassistant.ui.theme.DIMENS_24dp
import com.ibsystem.temiassistant.ui.theme.DIMENS_8dp
import com.ibsystem.temiassistant.ui.theme.Maroon
import com.ibsystem.temiassistant.ui.theme.Transparent
import com.ibsystem.temiassistant.ui.theme.White

@Composable
fun SettingsScreen(navController: NavController) {
    val viewModel = SettingsViewModel.getInstance()
    Scaffold(
        topBar = {
            TopBarSection(
                username = "Map",
                onBack = { navController.navigateUp() }
            )
        }
    ) {
        GifImage(
            modifier = Modifier.fillMaxSize(),
            gif = R.drawable.lantern
        )

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(it)
                .padding(DIMENS_16dp)
        ) {
            SettingsSwitchComp(
                name = "Robot Speaker",
                icon = R.drawable.ic_final_icon,
                iconDesc = "SwitchComp",
                state = viewModel.isSpeakerOn.collectAsState()
            ) {
                viewModel.toggleSwitch(SettingsViewModel.SPEAKER_SWITCH)
            }

            SettingsSwitchComp(
                name = "Robot Detection",
                icon = R.drawable.ic_final_icon,
                iconDesc = "SwitchComp",
                state = viewModel.isDetectionOn.collectAsState()
            ) {
                viewModel.toggleSwitch(SettingsViewModel.DETECTION_SWITCH)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsSwitchComp(
    @DrawableRes icon: Int,
    iconDesc: String,
    name: String,
    state: State<Boolean>,
    onClick: () -> Unit
) {
    Surface(
        color = Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(DIMENS_16dp),
        onClick = onClick,
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painterResource(id = icon),
                        contentDescription = iconDesc,
                        modifier = Modifier.size(DIMENS_24dp),
                        tint = White
                    )
                    Spacer(modifier = Modifier.width(DIMENS_8dp))
                    TextGradient(
                        text = name,
                        gradientColors = listOf(Maroon, Chocolate),
                        roundedCornerShape = RoundedCornerShape(DIMENS_1dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = state.value,
                    onCheckedChange = { onClick() }
                )
            }
            Divider()
        }
    }
}