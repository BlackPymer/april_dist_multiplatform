package dev.yarobot.shirmaz.camera

import android.view.SurfaceView
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
actual fun ModelView() {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    AndroidView(factory = { context ->
        val renderer = ModelRenderer()

        SurfaceView(context).apply {
            renderer.onSurfaceAvailable(this, lifecycle)
        }
    })
}