package dev.yarobot.shirmaz.camera.model

import android.view.SurfaceView
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import dev.yarobot.shirmaz.camera.CameraScreenState
import dev.yarobot.shirmaz.camera.ModelRenderer

@Composable
actual fun ModelView(state: CameraScreenState) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    AndroidView(factory = { context ->
        SurfaceView(context).apply {
            val renderer = state.currentModel?.let {
                ModelRenderer(
                    surfaceView = this,
                    lifecycle = lifecycle,
                    model = it
                )
            }
            renderer?.bindBones(state.modelPosition)
            renderer?.onSurfaceAvailable()
        }
    })
}