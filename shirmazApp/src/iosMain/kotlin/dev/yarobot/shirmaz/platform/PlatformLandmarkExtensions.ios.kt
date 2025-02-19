@file:OptIn(ExperimentalForeignApi::class)

package dev.yarobot.shirmaz.platform

import cocoapods.MLKitPoseDetectionCommon.*
import cocoapods.MLKitVision.MLKVision3DPoint
import kotlinx.cinterop.ExperimentalForeignApi

actual val NOSE: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeNose ?: ""
actual val LEFT_EYE_INNER: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeLeftEyeInner ?: ""
actual val LEFT_EYE: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeLeftEye ?: ""
actual val LEFT_EYE_OUTER: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeLeftEyeOuter ?: ""
actual val RIGHT_EYE_INNER: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeRightEyeInner ?: ""
actual val RIGHT_EYE: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeRightEye ?: ""
actual val RIGHT_EYE_OUTER: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeRightEyeOuter ?: ""
actual val LEFT_EAR: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeLeftEar ?: ""
actual val RIGHT_EAR: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeRightEar ?: ""
actual val LEFT_MOUTH: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeMouthLeft ?: ""
actual val RIGHT_MOUTH: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeMouthRight ?: ""
actual val LEFT_SHOULDER: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeLeftShoulder ?: ""
actual val RIGHT_SHOULDER: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeRightShoulder ?: ""
actual val LEFT_ELBOW: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeLeftElbow ?: ""
actual val RIGHT_ELBOW: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeRightElbow ?: ""
actual val LEFT_WRIST: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeLeftWrist ?: ""
actual val RIGHT_WRIST: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeRightWrist ?: ""
actual val LEFT_PINKY: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeLeftPinkyFinger ?: ""
actual val RIGHT_PINKY: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeRightPinkyFinger ?: ""
actual val LEFT_INDEX: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeLeftIndexFinger ?: ""
actual val RIGHT_INDEX: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeRightIndexFinger ?: ""
actual val LEFT_THUMB: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeLeftThumb ?: ""
actual val RIGHT_THUMB: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeRightThumb ?: ""
actual val LEFT_HIP: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeLeftHip ?: ""
actual val RIGHT_HIP: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeRightHip ?: ""
actual val LEFT_KNEE: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeLeftKnee ?: ""
actual val RIGHT_KNEE: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeRightKnee ?: ""
actual val LEFT_ANKLE: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeLeftAnkle ?: ""
actual val RIGHT_ANKLE: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeRightAnkle ?: ""
actual val LEFT_HEEL: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeLeftHeel ?: ""
actual val RIGHT_HEEL: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeRightHeel ?: ""
actual val LEFT_FOOT_INDEX: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeLeftToe ?: ""
actual val RIGHT_FOOT_INDEX: PlatformLandmarkType
    get() = MLKPoseLandmarkTypeRightToe ?: ""

@OptIn(ExperimentalForeignApi::class)
actual fun PlatformLandmark.float3DPose(): Float3Dimension = Float3Dimension(
    x = (this.position as MLKVision3DPoint).x.toFloat(),
    y = (this.position as MLKVision3DPoint).y.toFloat(),
    z = (this.position as MLKVision3DPoint).z.toFloat()
)

actual val PlatformLandmark.type: PlatformLandmarkType
    get() = this.type as PlatformLandmarkType
