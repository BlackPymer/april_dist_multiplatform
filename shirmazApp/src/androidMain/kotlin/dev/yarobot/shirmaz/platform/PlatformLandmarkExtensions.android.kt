package dev.yarobot.shirmaz.platform

import com.google.mlkit.vision.pose.PoseLandmark

val NOSE: PlatformLandmarkType
    get() = PoseLandmark.NOSE
val LEFT_EYE_INNER: PlatformLandmarkType
    get() = PoseLandmark.LEFT_EYE_INNER
val LEFT_EYE: PlatformLandmarkType
    get() = PoseLandmark.LEFT_EYE
val LEFT_EYE_OUTER: PlatformLandmarkType
    get() = PoseLandmark.LEFT_EYE_OUTER
val RIGHT_EYE_INNER: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_EYE_INNER
val RIGHT_EYE: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_EYE
val RIGHT_EYE_OUTER: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_EYE_OUTER
val LEFT_EAR: PlatformLandmarkType
    get() = PoseLandmark.LEFT_EAR
val RIGHT_EAR: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_EAR
val LEFT_MOUTH: PlatformLandmarkType
    get() = PoseLandmark.LEFT_MOUTH
val RIGHT_MOUTH: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_MOUTH
val LEFT_SHOULDER: PlatformLandmarkType
    get() = PoseLandmark.LEFT_SHOULDER
val RIGHT_SHOULDER: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_SHOULDER
val LEFT_ELBOW: PlatformLandmarkType
    get() = PoseLandmark.LEFT_ELBOW
val RIGHT_ELBOW: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_ELBOW
val LEFT_WRIST: PlatformLandmarkType
    get() = PoseLandmark.LEFT_WRIST
val RIGHT_WRIST: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_WRIST
val LEFT_PINKY: PlatformLandmarkType
    get() = PoseLandmark.LEFT_PINKY
val RIGHT_PINKY: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_PINKY
val LEFT_INDEX: PlatformLandmarkType
    get() = PoseLandmark.LEFT_INDEX
val RIGHT_INDEX: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_INDEX
val LEFT_THUMB: PlatformLandmarkType
    get() = PoseLandmark.LEFT_THUMB
val RIGHT_THUMB: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_THUMB
val LEFT_HIP: PlatformLandmarkType
    get() = PoseLandmark.LEFT_HIP
val RIGHT_HIP: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_HIP
val LEFT_KNEE: PlatformLandmarkType
    get() = PoseLandmark.LEFT_KNEE
val RIGHT_KNEE: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_KNEE
val LEFT_ANKLE: PlatformLandmarkType
    get() = PoseLandmark.LEFT_ANKLE
val RIGHT_ANKLE: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_ANKLE
val LEFT_HEEL: PlatformLandmarkType
    get() = PoseLandmark.LEFT_HEEL
val RIGHT_HEEL: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_HEEL
val LEFT_FOOT_INDEX: PlatformLandmarkType
    get() = PoseLandmark.LEFT_FOOT_INDEX
val RIGHT_FOOT_INDEX: PlatformLandmarkType
    get() = PoseLandmark.RIGHT_FOOT_INDEX


fun PlatformLandmark.float3DPose(): Float3Dimension =
    Float3Dimension(
        x = this.position3D.x,
        y = this.position3D.y,
        z = this.position3D.z
    )

val PlatformLandmark.type: PlatformLandmarkType
    get() = this.landmarkType