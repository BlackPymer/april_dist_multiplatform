package dev.yarobot.shirmaz.render

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.mlkit.vision.common.PointF3D
import dev.yarobot.shirmaz.camera.Bones
import dev.yarobot.shirmaz.camera.model.CameraSize
import dev.yarobot.shirmaz.camera.model.ThreeDModel
import dev.yarobot.shirmaz.platform.LEFT_ELBOW
import dev.yarobot.shirmaz.platform.LEFT_HIP
import dev.yarobot.shirmaz.platform.LEFT_SHOULDER
import dev.yarobot.shirmaz.platform.PlatformImage
import dev.yarobot.shirmaz.platform.RIGHT_ELBOW
import dev.yarobot.shirmaz.platform.RIGHT_HIP
import dev.yarobot.shirmaz.platform.RIGHT_SHOULDER
import dev.yarobot.shirmaz.posedetection.ShirmazPoseDetector
import io.github.sceneview.Scene
import io.github.sceneview.gesture.GestureDetector.SimpleOnGestureListener
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
import io.github.sceneview.rememberOnGestureListener
import java.nio.ByteBuffer
import kotlin.math.PI
import kotlin.math.atan
import android.graphics.PixelFormat
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex


actual fun createModelView(
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
        modelNode.position = Position(x = -0.42f, y = 1.05f, z = 0f)
        Scene(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
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
                setZOrderMediaOverlay(true)
                holder.setFormat(PixelFormat.TRANSLUCENT)
            },
            childNodes = listOf(
                centerNode,
                rememberNode { modelNode }
            ),
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
            },
            onGestureListener = rememberOnGestureListener(
                creator = { SimpleOnGestureListener() }
            )
        )
    }

    override fun updateModelPosition(image: PlatformImage) {
        detectPose(image = image)
    }

    private fun detectPose(image: PlatformImage) {
        poseDetector.processImage(image) { poses, error ->
            isPoseValid = !poses.isNullOrEmpty()
            if (!poses.isNullOrEmpty()) {
                println(poses[RIGHT_SHOULDER].position3D)
                println(poses[LEFT_SHOULDER].position3D)
                println("!!!! ${image.width} w ${image.height} h")
                modelScale = calculateScale(
                    leftShoulder = poses[RIGHT_SHOULDER].position3D,
                    rightShoulder = poses[LEFT_SHOULDER].position3D,
                    spine = averageOf(poses[LEFT_HIP].position3D, poses[24].position3D),
                )
                spinePosition = averageOf(poses[LEFT_HIP].position3D, poses[RIGHT_HIP].position3D)
                    .toPosition()
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
                ).toPosition() - spinePosition
            }
        }
    }

    private fun calculateScale(
        leftShoulder: PointF3D,
        rightShoulder: PointF3D,
        spine: PointF3D,
    ): Scale {
        val shoulderDistance = leftShoulder.x - rightShoulder.x
        val height = spine.y - averageOf(leftShoulder, rightShoulder).y
        return Scale(
            x = -defaultModelScale.x * shoulderDistance / defaultShoulderDistance / CameraSize.WIDTH * CameraSize.WIDTH,
            y = defaultModelScale.y * height / defaultHeight / CameraSize.HEIGHT * CameraSize.HEIGHT,
            z = defaultModelScale.z
        )
    }

    private fun averageOf(firstPoint: PointF3D, secondPoint: PointF3D) =
        PointF3D.from(
            (firstPoint.x + secondPoint.x) / 2,
            (firstPoint.y + secondPoint.y) / 2,
            (firstPoint.z + secondPoint.z) / 2
        )

    private fun PointF3D.toPosition(): Position {
        val maxValue = Position(2.8f, -7.2f, 0f)
        return Position(
            maxValue.x * this.x / CameraSize.HEIGHT * defaultModelScale.x / modelScale.x,
            maxValue.y * this.y / CameraSize.WIDTH * defaultModelScale.y / modelScale.y,
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
