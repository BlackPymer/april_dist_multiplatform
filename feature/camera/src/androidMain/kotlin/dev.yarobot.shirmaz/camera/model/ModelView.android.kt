package dev.yarobot.shirmaz.camera.model

import android.view.SurfaceView
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import dev.yarobot.shirmaz.camera.ModelRenderer

@Composable
actual fun ModelView() {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(factory = { context ->
        val renderer = ModelRenderer()

        SurfaceView(context).apply {
            renderer.onSurfaceAvailable(this, lifecycleOwner)
        }
    })
}