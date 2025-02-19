package dev.yarobot.shirmaz.render

import android.view.SurfaceView
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import dev.yarobot.shirmaz.camera.CameraScreenState
import dev.yarobot.shirmaz.platform.PlatformImage
import dev.yarobot.shirmaz.posedetection.ShirmazPoseDetectorOptions
import dev.yarobot.shirmaz.posedetection.createPoseDetector

actual fun createModelView(
    screenHeight: Float,
    screenWidth: Float
): ModelView = AndroidModelView(
        screenWidth = screenWidth,
        screenHeight = screenHeight
    )


private class AndroidModelView(
    override val screenHeight: Float,
    override val screenWidth: Float
): ModelView {
    private val poseDetector = createPoseDetector(ShirmazPoseDetectorOptions.STREAM)

    private var modelRenderer: ModelRenderer? = null

    @Composable
    override fun ModelRendererInit(state: CameraScreenState) {
        println("!!!!! modelRendererInit")
        val lifecycle = LocalLifecycleOwner.current.lifecycle
        AndroidView(factory = { context ->
            SurfaceView(context).apply {
                modelRenderer = state.currentModel?.let { model ->
                    createModelRenderer(
                        model = model,
                        platformRendererConfiguration = PlatformRendererConfiguration(
                            lifecycle = lifecycle,
                            surfaceView = this
                        )
                    )
                }
                modelRenderer?.onSurfaceAvailable()
            }
        })
    }


    override fun updateModelPosition(image: PlatformImage) {
        detectPose(
            image = image,
            imageHeight = image.height.toFloat(),
            imageWidth = image.width.toFloat()
        )
    }

    private fun detectPose(image: PlatformImage, imageHeight: Float, imageWidth: Float) {
        poseDetector.processImage(image) { poses, error ->
            modelRenderer?.bindBones(
                modelPosition = poses,
                imageHeight = imageHeight,
                imageWidth = imageWidth
            )
            error?.let {
                println(it)
            }
        }
    }
}