package dev.yarobot.shirmaz.camera

import android.annotation.SuppressLint
import android.view.Choreographer
import android.view.SurfaceView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.android.filament.View
import com.google.android.filament.android.UiHelper
import com.google.android.filament.utils.Float3
import com.google.android.filament.utils.ModelViewer
import dev.yarobot.shirmaz.camera.model.ThreeDModel
import java.nio.ByteBuffer
import dev.yarobot.shirmaz.platform.PlatformLandmark

actual class ModelRenderer actual constructor(
    surfaceView: Any,
    lifecycle: Any,
    private val model: ThreeDModel,
    private val screenHeight: Float,
    private val screenWidth: Float
) {

    private val surfaceView1: SurfaceView = surfaceView as SurfaceView
    private val lifecycle1: Lifecycle = lifecycle as Lifecycle

    private val choreographer: Choreographer = Choreographer.getInstance()

    private val uiHelper: UiHelper = UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK).apply {
        isOpaque = false
    }

    private val modelViewer: ModelViewer =
        ModelViewer(surfaceView = surfaceView1, uiHelper = uiHelper)

    private val frameScheduler = FrameCallback()

    private val boneController = BoneController(modelViewer)

    private val lifecycleObserver = object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            choreographer.postFrameCallback(frameScheduler)
        }

        override fun onPause(owner: LifecycleOwner) {
            choreographer.removeFrameCallback(frameScheduler)
        }

        override fun onDestroy(owner: LifecycleOwner) {
            choreographer.removeFrameCallback(frameScheduler)
            lifecycle1.removeObserver(this)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    actual fun onSurfaceAvailable() {
        lifecycle1.addObserver(lifecycleObserver)

        surfaceView1.setOnTouchListener { _, event ->
            modelViewer.onTouchEvent(event)
            true
        }

        setUpModelViewer()
        modelOpen(Float3(-1f, 23 / 9f, -4f))
    }

    actual fun bindBones(
        modelPosition: List<PlatformLandmark>?,
        imageWidth: Float,
        imageHeight: Float
    ) {
        with(boneController) {
            if (modelPosition != null) {
                val centerPoint = convertPixelToBonesCoordinates(modelPosition.let { list ->
                    if (list.size > 24) {
                        val pos1 = convertNNPointToScreenPixels(
                            nnPoint = Float3(
                                list[23].position3D.x,
                                list[23].position3D.y,
                                list[23].position3D.z
                            ),
                            imageWidth = imageWidth,
                            imageHeight = imageHeight,
                            screenWidth = screenWidth,
                            screenHeight = screenHeight
                        )
                        val pos2 = convertNNPointToScreenPixels(
                            nnPoint = Float3(
                                list[24].position3D.x,
                                list[24].position3D.y,
                                list[24].position3D.z
                            ),
                            imageWidth = imageWidth,
                            imageHeight = imageHeight,
                            screenWidth = screenWidth,
                            screenHeight = screenHeight
                        )
                        Float3(
                            x = (pos1.x + pos2.x) / 2,
                            y = (pos1.y + pos2.y) / 2,
                            z = (pos1.z + pos2.z) / 2
                        )
                    } else {
                        Float3(0f, 0f, -4f)

                    }
                }
                )
                Bones.spine.position = centerPoint
                Bones.leftHand.rotation = Float3(0f,0f,5f)
                Bones.rightShoulder.position = modelPosition.let { list ->
                        convertPixelToBonesCoordinates(
                        convertNNPointToScreenPixels(
                            nnPoint = Float3(
                                list[12].position3D.x - list[24].position3D.x,
                                list[12].position3D.y - list[24].position3D.y,
                                list[12].position3D.z + list[24].position3D.z
                            ),
                            imageWidth = imageWidth,
                            imageHeight = imageHeight,
                            screenWidth = screenWidth,
                            screenHeight = screenHeight,
                            invertY = false
                        )
                    )
                }
                Bones.leftShoulder.position = modelPosition.let { list ->
                    convertPixelToBonesCoordinates(
                        convertNNPointToScreenPixels(
                            nnPoint = Float3(
                                list[11].position3D.x - list[24].position3D.x,
                                list[11].position3D.y - list[24].position3D.y,
                                list[11].position3D.z + list[24].position3D.z
                            ),
                            imageWidth = imageWidth,
                            imageHeight = imageHeight,
                            screenWidth = screenWidth,
                            screenHeight = screenHeight,
                            invertY = false
                        )
                    )
                }

            } else {
                println("!!!modelPosition is null")
            }
        }
    }


    private fun convertPixelToBonesCoordinates(
        pixel: Float3
    ): Float3 {
        val mainBonePadding = Float3(33f, -35f, 0f)
        val ratio = screenWidth / 550f
        val newPosition = Float3(
            x = pixel.x / ratio + mainBonePadding.x,
            y = -pixel.y / ratio + mainBonePadding.y,
            z = -pixel.z / ratio + mainBonePadding.z
        )
        if (newPosition.x.isNaN() || newPosition.y.isNaN() || newPosition.z.isNaN()) {
            println("!!! ERROR: New position for Spine contains NaN: $newPosition")
        } else {
            return newPosition
        }
        return Float3(0f, 0f, 0f)
    }

    private fun convertNNPointToScreenPixels(
        nnPoint: Float3,
        imageWidth: Float,
        imageHeight: Float,
        screenWidth: Float,
        screenHeight: Float,
        invertY: Boolean = true
    ): Float3 {
        val scale = maxOf(screenWidth / imageWidth, screenHeight / imageHeight)
        val scaledImageHeight = imageHeight * scale

        val offsetY = (screenHeight - scaledImageHeight) / 2f

        val screenY = if (invertY) {
            (imageHeight - nnPoint.y) * scale + offsetY
        } else {
            nnPoint.y * scale + offsetY
        }
        return Float3(nnPoint.x, screenY, nnPoint.z)
    }


    private fun modelOpen(centerPoint: Float3) {
        val byteBuffer = ByteBuffer.wrap(model.bytes)
        modelViewer.loadModelGlb(byteBuffer)
        modelViewer.transformToUnitCube(centerPoint = centerPoint)
    }

    private fun setUpModelViewer() = modelViewer.apply {
        scene.skybox = null
        view.blendMode = View.BlendMode.TRANSLUCENT
        renderer.clearOptions = renderer.clearOptions.apply {
            clear = true
        }
        view.renderQuality.hdrColorBuffer = View.QualityLevel.LOW
    }

    inner class FrameCallback : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            choreographer.postFrameCallback(this)
            modelViewer.render(frameTimeNanos)
        }
    }
}
