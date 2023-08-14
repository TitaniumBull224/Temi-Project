package com.ibsystem.temiassistant.presentation.common.component

import android.content.Context
import android.net.ConnectivityManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.ibsystem.temiassistant.R
import com.ibsystem.temiassistant.ui.theme.Black
import com.ibsystem.temiassistant.ui.theme.DIMENS_40dp
import com.ibsystem.temiassistant.ui.theme.DIMENS_4dp
import com.ibsystem.temiassistant.ui.theme.DIMENS_64dp
import com.ibsystem.temiassistant.ui.theme.DIMENS_8dp
import com.ibsystem.temiassistant.ui.theme.Maroon
import com.ibsystem.temiassistant.ui.theme.TEXT_SIZE_12sp

@Suppress("DEPRECATION")
@Composable
fun TopBarSection(
    username: String,
    profile: Painter = painterResource(id = R.drawable.ic_temi),
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val isOnline = connectivityManager.activeNetworkInfo != null

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(DIMENS_64dp),
        backgroundColor = Black,
        elevation = DIMENS_4dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = DIMENS_8dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack
            ){
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Maroon
                )
            }


            Spacer(modifier = Modifier.width(DIMENS_8dp))

            Image(
                painter = profile,
                contentDescription = null,
                modifier = Modifier
                    .size(DIMENS_40dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(DIMENS_8dp))

            Column {
                Text(text = username, fontWeight = FontWeight.SemiBold, color = Color.Red)
                Text(
                    text = if (isOnline) "Online" else "Offline",
                    fontSize = TEXT_SIZE_12sp,
                    color = Maroon
                )
            }
        }
    }
}


@Preview
@Composable
fun TopBarSectionPreview() {
    TopBarSection(
        username = "FUWAMOCO",
        onBack = {}
    )
}

