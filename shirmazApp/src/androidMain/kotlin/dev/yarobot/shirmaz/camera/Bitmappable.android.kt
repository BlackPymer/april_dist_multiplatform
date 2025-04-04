package dev.yarobot.shirmaz.camera

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.Modifier
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.roundToInt
import androidx.core.graphics.createBitmap

fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}


class BitmappableScopeImpl(
    private val view: View,
    private val bounds: Rect
) : BitmappableScope {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun convertContentToImageBitmap(): ImageBitmap =
        suspendCancellableCoroutine { continuation ->
            val width = bounds.width.roundToInt()
            val height = bounds.height.roundToInt()

            val bmp = createBitmap(width, height)
            val activity = view.context.getActivity() ?: error("Activity not found")

            val rect = android.graphics.Rect(
                bounds.left.toInt(),
                bounds.top.toInt(),
                bounds.right.toInt(),
                bounds.bottom.toInt()
            )

            PixelCopy.request(
                activity.window,
                rect,
                bmp,
                { copyResult ->
                    if (copyResult == PixelCopy.SUCCESS) {
                        continuation.resume(bmp.asImageBitmap())
                    } else {
                        continuation.resumeWithException(Exception("PixelCopy failed with error code $copyResult"))
                    }
                },
                Handler(Looper.getMainLooper())
            )
        }

}

@Composable
actual fun Bitmappable(
    modifier: Modifier,
    content: @Composable BitmappableScope.() -> Unit
) {
    val contentBounds = remember { mutableStateOf<Rect?>(null) }
    val view = LocalView.current

    Box(
        modifier = modifier.onGloballyPositioned { coordinates ->
            contentBounds.value = coordinates.boundsInWindow()
            println("!!Bitmappable bounds updated: ${contentBounds.value}")
        }
    ) {
        contentBounds.value?.let { bounds ->
            if (bounds.width > 0 && bounds.height > 0) {
                val scope = BitmappableScopeImpl(view, bounds)
                println("!!BitmappableScope created with bounds: $bounds")
                with(scope) { content() }
            }
        }
    }
}


