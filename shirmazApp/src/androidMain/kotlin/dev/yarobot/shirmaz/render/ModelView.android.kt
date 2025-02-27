package dev.yarobot.shirmaz.render

import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.filament.Engine
import com.google.android.filament.utils.Float2
import com.google.android.filament.utils.pow
import com.google.ar.core.Anchor
import com.google.mlkit.vision.common.PointF3D
import dev.yarobot.shirmaz.camera.Bones
import dev.yarobot.shirmaz.camera.model.ThreeDModel
import dev.yarobot.shirmaz.platform.PlatformImage
import dev.yarobot.shirmaz.posedetection.ShirmazPoseDetectorOptions
import dev.yarobot.shirmaz.posedetection.createPoseDetector
import io.github.sceneview.Scene
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
import kotlin.math.PI
import kotlin.math.atan

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

    private val leftArmDefaultRotation = Rotation(0f, 0f, -90f)
    private val rightArmDefaultRotation = Rotation(0f, 0f, 90f)

    private var leftArmRotation = mutableStateOf(leftArmDefaultRotation)
    private var rightArmRotation = mutableStateOf(rightArmDefaultRotation)

    private var spinePosition = mutableStateOf(Position(0f, 0f, 0f))

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

        val modelNode = ModelNode(
            modelInstance = modelLoader.createModelInstance(ByteBuffer.wrap(model.bytes)),
            scaleToUnits = 18f
        )
        modelNode.position = Position(x = -0.42f, y = 1f, z = 0f)
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
            onViewCreated = {
                setZOrderOnTop(false)
            },
            childNodes = listOf(
                centerNode,
                rememberNode { modelNode }
            ),
            environmentLoader = environmentLoader,
            onFrame = {
                modelNode.nodes.forEach {
                    when (it.name) {
                        Bones.leftArm -> it.rotation = leftArmRotation.value
                        Bones.rightArm -> it.rotation = rightArmRotation.value
                        Bones.spine -> it.position = spinePosition.value
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
        detectPose(image = image)
    }

    private fun detectPose(image: PlatformImage) {
        poseDetector.processImage(image) { poses, error ->
            error?.let {
                println(it)
            }
            poses?.forEachIndexed { index, it -> println("!!${it.position3D} - $index") }
            println("!!${image.height}x${image.width}")
            println("!!screen: $screenHeight x $screenWidth")
            if (poses != null && poses.size > 24) {
                spinePosition.value =
                    convertToBones(
                        average(poses[23].position3D, poses[24].position3D),
                        imageWidth = image.height.toFloat(),
                        imageHeight = image.width.toFloat()
                    )
                leftArmRotation.value = calculateAngle(
                    poses[11].position3D,
                    poses[13].position3D,
                    leftArmDefaultRotation
                )

                rightArmRotation.value = calculateAngle(
                    poses[12].position3D,
                    poses[14].position3D,
                    rightArmDefaultRotation
                ) - Position(0f, 0f, 180f)
            }
        }
    }


    private fun average(point1: PointF3D, point2: PointF3D) =
        PointF3D.from(
            (point1.x + point2.x) / 2,
            (point1.y + point2.y) / 2,
            (point1.z + point2.z) / 2
        )


    private fun convertToBones(point: PointF3D, imageHeight: Float, imageWidth: Float): Position {
        val maxValue = Position(2.8f, -6.1f, 0f)

        return Position(
            maxValue.x * point.x / imageWidth,
            maxValue.y * point.y / imageHeight,
            0f
        )
    }

    private fun calculateAngle(point1: PointF3D, point2: PointF3D, defaultRotation: Rotation) =
        Rotation(
            defaultRotation.x,
            defaultRotation.y,
            defaultRotation.z + (90 - atan((point2.y - point1.y) / (point2.x - point1.x)) * 180f / PI.toFloat())
        )
}