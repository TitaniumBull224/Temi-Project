package com.ibsystem.temiassistant.presentation.common.component

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size

@JvmOverloads
@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    gif: Any
) {
    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader
            .Builder(context)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }
    Image(
        painter = rememberAsyncImagePainter(
            model = ImageRequest
                .Builder(context)
                .data(data = gif)
                .apply(
                    block = {
                        size(Size.ORIGINAL)
                    }
                )
                .build(),
            imageLoader = imageLoader
        ),
        contentDescription = "Background GIF",
        contentScale = ContentScale.Crop,
        modifier = modifier.fillMaxWidth(),
    )
}