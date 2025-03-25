package dev.yarobot.shirmaz.camera

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap

@Stable
interface BitmappableScope {
    @Stable
    suspend fun convertContentToImageBitmap(): ImageBitmap
}


@Composable
expect fun Bitmappable(
    modifier: Modifier = Modifier,
    content: @Composable BitmappableScope.() -> Unit
)
