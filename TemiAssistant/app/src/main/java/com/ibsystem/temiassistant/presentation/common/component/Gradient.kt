package com.ibsystem.temiassistant.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.ibsystem.temiassistant.ui.theme.Crimson
import com.ibsystem.temiassistant.ui.theme.DIMENS_16dp
import com.ibsystem.temiassistant.ui.theme.DIMENS_32dp
import com.ibsystem.temiassistant.ui.theme.DIMENS_64dp
import com.ibsystem.temiassistant.ui.theme.DIMENS_8dp
import com.ibsystem.temiassistant.ui.theme.DarkBlue
import com.ibsystem.temiassistant.ui.theme.DarkOrange
import com.ibsystem.temiassistant.ui.theme.GilroyFontFamily
import com.ibsystem.temiassistant.ui.theme.Purple
import com.ibsystem.temiassistant.ui.theme.TEXT_SIZE_24sp
import com.ibsystem.temiassistant.ui.theme.White

@Composable
fun ButtonGradient(
    modifier: Modifier = Modifier,
    name: String,
    onClick: () -> Unit,
    gradientColors: List<Color> = listOf(Crimson, DarkOrange),
    roundedCornerShape: RoundedCornerShape = RoundedCornerShape(topEnd = DIMENS_32dp, bottomStart = DIMENS_32dp)
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.horizontalGradient(colors = gradientColors),
                shape = roundedCornerShape
            )
            .clip(roundedCornerShape)
            .clickable(onClick = onClick)
            .padding(PaddingValues(horizontal = DIMENS_64dp, vertical = DIMENS_16dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            fontSize = TEXT_SIZE_24sp,
            color = White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TextGradient(
    modifier: Modifier = Modifier,
    text: String,
    gradientColors: List<Color> = listOf(Crimson, DarkOrange),
    roundedCornerShape: RoundedCornerShape = RoundedCornerShape(topEnd = DIMENS_32dp, bottomStart = DIMENS_32dp)
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.horizontalGradient(colors = gradientColors),
                shape = roundedCornerShape
            )
            .clip(roundedCornerShape)
            .padding(PaddingValues(horizontal = DIMENS_16dp, vertical = DIMENS_8dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = GilroyFontFamily,
            fontSize = TEXT_SIZE_24sp,
            color = White,
            fontWeight = FontWeight.Bold
        )
    }
}