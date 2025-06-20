package dev.yarobot.shirmaz.render

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.mlkit.vision.common.PointF3D
import dev.yarobot.shirmaz.camera.AppMode
import dev.yarobot.shirmaz.camera.Bones
import dev.yarobot.shirmaz.camera.CameraIntent
import dev.yarobot.shirmaz.camera.CameraScreenState
import dev.yarobot.shirmaz.camera.model.CameraSize
import dev.yarobot.shirmaz.platform.LEFT_ELBOW
import dev.yarobot.shirmaz.platform.LEFT_HIP
import dev.yarobot.shirmaz.platform.LEFT_SHOULDER
import dev.yarobot.shirmaz.platform.PlatformImage
import dev.yarobot.shirmaz.platform.PlatformInputImage
import dev.yarobot.shirmaz.platform.RIGHT_ELBOW
import dev.yarobot.shirmaz.platform.RIGHT_HIP
import dev.yarobot.shirmaz.platform.RIGHT_SHOULDER
import dev.yarobot.shirmaz.posedetection.ShirmazPoseDetector
import io.github.sceneview.Scene
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Scale
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import kotlin.math.PI
import kotlin.math.atan

interface ModelView {
    val poseDetector: ShirmazPoseDetector

    @Composable
    fun ModelRendererInit(
        state: CameraScreenState,
        modifier: Modifier,
        onIntent: (CameraIntent) -> Unit
    )

    fun updateModelPosition(image: PlatformImage)
    fun updateModelPosition(image: PlatformInputImage)
}

fun createModelView(
    poseDetector: ShirmazPoseDetector
): ModelView = AndroidModelView(
    poseDetector = poseDetector
)

private class AndroidModelView(
    override val poseDetector: ShirmazPoseDetector
) : ModelView {

    private val leftArmDefaultRotation = Rotation(0f, 0f, -90f)
    private val rightArmDefaultRotation = Rotation(0f, 0f, 90f)
    private val defaultModelScale = Scale(x = 0.0065205214f, y = 0.0065205214f, z = 0.003f)
    private val defaultShoulderDistance = 145f
    private val defaultHeight = 230f

    private var leftArmRotation = leftArmDefaultRotation
    private var rightArmRotation = rightArmDefaultRotation
    private var spinePosition = Position(0f, -6.2f, 0f)
    private var shoulderPosition = Position(0f, 2f, 0f)
    private var modelScale = defaultModelScale
    private var isPoseValid = false


    @Composable
    override fun ModelRendererInit(
        state: CameraScreenState,
        modifier: Modifier,
        onIntent: (CameraIntent) -> Unit
    ) {
        val engine = rememberEngine()
        val modelLoader = rememberModelLoader(engine)
        val environmentLoader = rememberEnvironmentLoader(engine)

        val centerNode = rememberNode(engine)
        val cameraNode = rememberCameraNode(engine) {
            position = Position(y = -0.5f, z = 2.0f)
            lookAt(centerNode)
            centerNode.addChildNode(this)
        }

        val modelNode = remember(state.currentShirt) {
            state.currentShirt?.let { shirt ->
                ModelNode(
                    modelInstance = modelLoader.createModelInstance(assetFileLocation = shirt.modelName),
                    scaleToUnits = 18f
                ).apply {
                    position = Position(x = -0.42f, y = 1.05f, z = 0f)
                }
            }
        }
        val isOnTop = remember(state.appMode) { state.appMode == AppMode.StaticImage }
        if (modelNode != null) {
            Scene(
                modifier = modifier.fillMaxSize(),
                engine = engine,
                isOpaque = false,
                modelLoader = modelLoader,
                cameraNode = cameraNode,
                cameraManipulator = rememberCameraManipulator(
                    orbitHomePosition = cameraNode.worldPosition,
                    targetPosition = centerNode.worldPosition
                ),
                onViewCreated = {
                    setZOrderOnTop(isOnTop)
                    setOnTouchListener { _, _ -> true }
                },
                childNodes = listOf(centerNode, modelNode),
                environmentLoader = environmentLoader,
                onFrame = {
                    modelNode.apply {
                        isVisible = isPoseValid
                        scale = modelScale
                        nodes.forEach { node ->
                            when (node.name) {
                                Bones.leftArm -> node.rotation = leftArmRotation
                                Bones.rightArm -> node.rotation = rightArmRotation
                                Bones.leftShoulder -> node.position = shoulderPosition
                                Bones.rightShoulder -> node.position = shoulderPosition
                                Bones.spine -> node.position = spinePosition
                            }
                        }
                    }
                    cameraNode.lookAt(centerNode)
                    onIntent(CameraIntent.ViewCreated)
                }
            )
        }
    }

    override fun updateModelPosition(image: PlatformInputImage) {
        println("!! width: ${image.width}, height: ${image.height}")
        poseDetector.processImage(image) { poses, error ->
            isPoseValid = !poses.isNullOrEmpty()
            if (!poses.isNullOrEmpty()) {
                modelScale = calculateScale(
                    leftShoulder = poses[RIGHT_SHOULDER].position3D,
                    rightShoulder = poses[LEFT_SHOULDER].position3D,
                    spine = averageOf(poses[LEFT_HIP].position3D, poses[24].position3D),
                    cameraHeight = image.height, cameraWidth = image.width
                )
                spinePosition = averageOf(poses[LEFT_HIP].position3D, poses[RIGHT_HIP].position3D)
                    .toPosition(cameraHeight = image.height, cameraWidth = image.width)
                leftArmRotation = calculateAngle(
                    poses[LEFT_SHOULDER].position3D,
                    poses[LEFT_ELBOW].position3D,
                    leftArmDefaultRotation
                )
                rightArmRotation = calculateAngle(
                    poses[RIGHT_SHOULDER].position3D,
                    poses[RIGHT_ELBOW].position3D,
                    rightArmDefaultRotation
                ) - Position(0f, 0f, 180f)
                shoulderPosition = averageOf(
                    poses[RIGHT_SHOULDER].position3D,
                    poses[LEFT_SHOULDER].position3D
                ).toPosition(cameraHeight = image.height, cameraWidth = image.width) - spinePosition
            }
        }
    }

    override fun updateModelPosition(image: PlatformImage) {
        detectPose(image = image)
    }

    private fun detectPose(image: PlatformImage) {
        poseDetector.processImage(image) { poses, error ->
            isPoseValid = !poses.isNullOrEmpty()
            if (!poses.isNullOrEmpty()) {
                modelScale = calculateScale(
                    leftShoulder = poses[RIGHT_SHOULDER].position3D,
                    rightShoulder = poses[LEFT_SHOULDER].position3D,
                    spine = averageOf(poses[LEFT_HIP].position3D, poses[24].position3D),
                    cameraHeight = CameraSize.WIDTH,
                    cameraWidth = CameraSize.HEIGHT
                )
                spinePosition = averageOf(poses[LEFT_HIP].position3D, poses[RIGHT_HIP].position3D)
                    .toPosition(cameraHeight = CameraSize.WIDTH, cameraWidth = CameraSize.HEIGHT)
                leftArmRotation = calculateAngle(
                    poses[LEFT_SHOULDER].position3D,
                    poses[LEFT_ELBOW].position3D,
                    leftArmDefaultRotation
                )
                rightArmRotation = calculateAngle(
                    poses[RIGHT_SHOULDER].position3D,
                    poses[RIGHT_ELBOW].position3D,
                    rightArmDefaultRotation
                ) - Position(0f, 0f, 180f)
                shoulderPosition = averageOf(
                    poses[RIGHT_SHOULDER].position3D,
                    poses[LEFT_SHOULDER].position3D
                ).toPosition(
                    cameraHeight = CameraSize.WIDTH,
                    cameraWidth = CameraSize.HEIGHT
                ) - spinePosition
            }
        }
    }

    private fun calculateScale(
        leftShoulder: PointF3D,
        rightShoulder: PointF3D,
        spine: PointF3D,
        cameraWidth: Int,
        cameraHeight: Int
    ): Scale {
        val shoulderDistance = leftShoulder.x - rightShoulder.x
        val height = spine.y - averageOf(leftShoulder, rightShoulder).y
        return Scale(
            x = -defaultModelScale.x * shoulderDistance / defaultShoulderDistance / cameraWidth * cameraWidth,
            y = defaultModelScale.y * height / defaultHeight / cameraHeight * cameraHeight,
            z = defaultModelScale.z
        )
    }

    private fun averageOf(firstPoint: PointF3D, secondPoint: PointF3D) =
        PointF3D.from(
            (firstPoint.x + secondPoint.x) / 2,
            (firstPoint.y + secondPoint.y) / 2,
            (firstPoint.z + secondPoint.z) / 2
        )

    private fun PointF3D.toPosition(cameraWidth: Int, cameraHeight: Int): Position {
        val maxValue = Position(3f, -7f, 0f)
        return Position(
            maxValue.x * this.x / cameraWidth * defaultModelScale.x / modelScale.x,
            maxValue.y * this.y / cameraHeight * defaultModelScale.y / modelScale.y,
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
