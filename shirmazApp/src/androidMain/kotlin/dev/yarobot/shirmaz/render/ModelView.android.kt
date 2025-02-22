package dev.yarobot.shirmaz.render

import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import dev.yarobot.shirmaz.camera.Bones
import dev.yarobot.shirmaz.camera.model.ThreeDModel
import dev.yarobot.shirmaz.platform.PlatformImage
import dev.yarobot.shirmaz.posedetection.ShirmazPoseDetectorOptions
import dev.yarobot.shirmaz.posedetection.createPoseDetector
import io.github.sceneview.Scene
import io.github.sceneview.animation.Transition.animatePosition
import io.github.sceneview.animation.Transition.animateRotation
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.CubeNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import io.github.sceneview.rememberOnGestureListener
import java.nio.Buffer
import java.nio.ByteBuffer
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

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
) : ModelView {
    private val poseDetector = createPoseDetector(ShirmazPoseDetectorOptions.STREAM)

    private var modelRenderer: ModelRenderer? = null

    @Composable
    override fun ModelRendererInit(model: ThreeDModel) {
        val engine = rememberEngine()
        val modelLoader = rememberModelLoader(engine)
        val environmentLoader = rememberEnvironmentLoader(engine)

        val centerNode = rememberNode(engine)

        val cameraNode = rememberCameraNode(engine) {
            position = Position(y = -0.5f, z = 2.0f)
            lookAt(centerNode)
            centerNode.addChildNode(this)
        }

        val cameraTransition = rememberInfiniteTransition(label = "CameraTransition")
        val cameraRotation by cameraTransition.animateRotation(
            initialValue = Rotation(y = 0.0f),
            targetValue = Rotation(y = 90.0f),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 7.seconds.toInt(DurationUnit.MILLISECONDS))
            )
        )

        val spinePosition by cameraTransition.animatePosition(
            initialValue = Position(x = 0.0f, y = 0.0f, z = 0.0f),
            targetValue = Position(x = 0.1f, y = 0.1f, z = 0.1f),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 7.seconds.toInt(DurationUnit.MILLISECONDS))
            )
        )

        val modelNode = ModelNode(
            modelInstance = modelLoader.createModelInstance(ByteBuffer.wrap(model.bytes)),
            scaleToUnits = 0.5f
        )

        Scene(
            modifier = Modifier.fillMaxSize(),
            engine = engine,
            isOpaque = false,
            modelLoader = modelLoader,
            cameraNode = cameraNode,
            cameraManipulator = rememberCameraManipulator(
                orbitHomePosition = cameraNode.worldPosition,
                targetPosition = centerNode.worldPosition
            ),
            childNodes = listOf(
                centerNode,
                rememberNode { modelNode }
            ),
            environmentLoader = environmentLoader,
            onFrame = {
                modelNode.nodes.forEach {
                    if (it.name == Bones.leftShoulder) {
                        it.rotation = cameraRotation
                    }
                    if (it.name == Bones.spine){
                        it.position = spinePosition
                    }
                }
                cameraNode.lookAt(centerNode)
            },
            onGestureListener = rememberOnGestureListener(
                onDoubleTap = { _, node ->
                    node?.apply {
                        scale *= 2.0f
                    }
                }
            )
        )
    }

    fun createAnchorNode(
        engine: Engine,
        modelLoader: ModelLoader,
        materialLoader: MaterialLoader,
        anchor: Anchor,
        model: Buffer
    ): AnchorNode {
        val anchorNode = AnchorNode(engine = engine, anchor = anchor)
        val modelNode = ModelNode(
            modelInstance = modelLoader.createModelInstance(model),
            // Scale to fit in a 0.5 meters cube
            scaleToUnits = 0.5f
        ).apply {
            // Model Node needs to be editable for independent rotation from the anchor rotation
            isEditable = true
            editableScaleRange = 0.2f..0.75f
        }
        val boundingBoxNode = CubeNode(
            engine,
            size = modelNode.extents,
            center = modelNode.center,
            materialInstance = materialLoader.createColorInstance(Color.White.copy(alpha = 0.5f))
        ).apply {
            isVisible = false
        }
        modelNode.addChildNode(boundingBoxNode)
        anchorNode.addChildNode(modelNode)

        listOf(modelNode, anchorNode).forEach {
            it.onEditingChanged = { editingTransforms ->
                boundingBoxNode.isVisible = editingTransforms.isNotEmpty()
            }
        }
        return anchorNode
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