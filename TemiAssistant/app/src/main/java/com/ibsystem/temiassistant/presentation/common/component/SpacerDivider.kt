package com.ibsystem.temiassistant.presentation.common.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ibsystem.temiassistant.ui.theme.DIMENS_1dp
import com.ibsystem.temiassistant.ui.theme.DIMENS_8dp

@Composable
fun SpacerDividerContent(
    modifier: Modifier = Modifier
) {
    Spacer(modifier = modifier.height(DIMENS_8dp))

    Divider(modifier = modifier.height(DIMENS_1dp))

    Spacer(modifier = modifier.height(DIMENS_8dp))
}