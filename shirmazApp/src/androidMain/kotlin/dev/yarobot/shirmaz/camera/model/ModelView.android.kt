package dev.yarobot.shirmaz.camera.model

import android.view.SurfaceView
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import dev.yarobot.shirmaz.camera.CameraScreenState
import dev.yarobot.shirmaz.camera.ModelRenderer
import dev.yarobot.shirmaz.platform.PlatformImage
import dev.yarobot.shirmaz.platform.PlatformLandmark
import dev.yarobot.shirmaz.platform.float3DPose
import dev.yarobot.shirmaz.platform.type
import dev.yarobot.shirmaz.posedetection.ShirmazPoseDetectorOptions
import dev.yarobot.shirmaz.posedetection.createPoseDetector


actual class ModelView actual constructor(
    private val screenHeight: Float,
    private val screenWidth: Float
) {
    private val poseDetector = createPoseDetector(ShirmazPoseDetectorOptions.STREAM)

    private var modelRenderer: ModelRenderer? = null

    @Composable
    actual fun ModelRendererInit(state: CameraScreenState) {
        println("!!!!! modelRendererInit")
        val lifecycle = LocalLifecycleOwner.current.lifecycle
        AndroidView(factory = { context ->
            SurfaceView(context).apply {
                modelRenderer = state.currentModel?.let {
                    ModelRenderer(
                        surfaceView = this,
                        lifecycle = lifecycle,
                        model = it,
                        screenHeight = screenHeight,
                        screenWidth = screenWidth
                    )
                }
                modelRenderer?.onSurfaceAvailable()
            }
        })
        //modelRenderer?.onSurfaceAvailable()
    }


    actual fun updateModelPosition(image: PlatformImage) {

        detectPose(
            image = image,
            imageHeight = image.height.toFloat(),
            imageWidth = image.width.toFloat()
        )
    }

    private fun detectPose(image: PlatformImage, imageHeight: Float, imageWidth: Float) {
        poseDetector.processImage(image) { poses, error ->
            println("!!!!! start")
            poses?.let {
                it.forEach { pose ->
                    println("!!${pose.float3DPose()} ${pose.type}")
                }
                modelRenderer?.bindBones(
                    modelPosition = poses,
                    imageHeight = imageHeight,
                    imageWidth = imageWidth
                )
            }
            error?.let {
                println(it)
            }

        }
    }
}