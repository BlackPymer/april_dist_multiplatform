package dev.yarobot.shirmaz.platform

import com.google.mlkit.vision.pose.PoseLandmark

actual val NOSE: PlatformLandmarkType
    get() = PoseLandmark.NOSE
actual val LEFT_EYE_INNER: PlatformLandmarkType
    get() = PoseLandmark.LEFT_EYE_INNER
actual val LEFT_EYE: PlatformLandmarkType
    get() = PoseLandmark.LEFT_EYE
actual val LEFT_EYE_OUTER: PlatformLandmarkType
    get() = PoseLandmark.LEFT_EYE_OUTER
actual val RIGHT_EYE_INNER: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_EYE_INNER
actual val RIGHT_EYE: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_EYE
actual val RIGHT_EYE_OUTER: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_EYE_OUTER
actual val LEFT_EAR: PlatformLandmarkType
    get() = PoseLandmark.LEFT_EAR
actual val RIGHT_EAR: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_EAR
actual val LEFT_MOUTH: PlatformLandmarkType
    get() = PoseLandmark.LEFT_MOUTH
actual val RIGHT_MOUTH: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_MOUTH
actual val LEFT_SHOULDER: PlatformLandmarkType
    get() = PoseLandmark.LEFT_SHOULDER
actual val RIGHT_SHOULDER: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_SHOULDER
actual val LEFT_ELBOW: PlatformLandmarkType
    get() = PoseLandmark.LEFT_ELBOW
actual val RIGHT_ELBOW: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_ELBOW
actual val LEFT_WRIST: PlatformLandmarkType
    get() = PoseLandmark.LEFT_WRIST
actual val RIGHT_WRIST: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_WRIST
actual val LEFT_PINKY: PlatformLandmarkType
    get() = PoseLandmark.LEFT_PINKY
actual val RIGHT_PINKY: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_PINKY
actual val LEFT_INDEX: PlatformLandmarkType
    get() = PoseLandmark.LEFT_INDEX
actual val RIGHT_INDEX: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_INDEX
actual val LEFT_THUMB: PlatformLandmarkType
    get() = PoseLandmark.LEFT_THUMB
actual val RIGHT_THUMB: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_THUMB
actual val LEFT_HIP: PlatformLandmarkType
    get() = PoseLandmark.LEFT_HIP
actual val RIGHT_HIP: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_HIP
actual val LEFT_KNEE: PlatformLandmarkType
    get() = PoseLandmark.LEFT_KNEE
actual val RIGHT_KNEE: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_KNEE
actual val LEFT_ANKLE: PlatformLandmarkType
    get() = PoseLandmark.LEFT_ANKLE
actual val RIGHT_ANKLE: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_ANKLE
actual val LEFT_HEEL: PlatformLandmarkType
    get() = PoseLandmark.LEFT_HEEL
actual val RIGHT_HEEL: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_HEEL
actual val LEFT_FOOT_INDEX: PlatformLandmarkType
    get() = PoseLandmark.LEFT_FOOT_INDEX
actual val RIGHT_FOOT_INDEX: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_FOOT_INDEX


actual fun PlatformLandmark.float3DPose(): Float3Dimension =
    Float3Dimension(
        x = this.position3D.x,
        y = this.position3D.y,
        z = this.position3D.z
    )

actual val PlatformLandmark.type: PlatformLandmarkType
    get() = this.landmarkType