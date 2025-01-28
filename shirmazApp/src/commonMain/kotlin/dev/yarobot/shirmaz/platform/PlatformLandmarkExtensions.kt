package dev.yarobot.shirmaz.platform

expect val NOSE: PlatformLandmarkType?
expect val LEFT_EYE_INNER: PlatformLandmarkType?
expect val LEFT_EYE: PlatformLandmarkType?
expect val LEFT_EYE_OUTER: PlatformLandmarkType?
expect val RIGHT_EYE_INNER: PlatformLandmarkType?
expect val RIGHT_EYE: PlatformLandmarkType?
expect val RIGHT_EYE_OUTER: PlatformLandmarkType?
expect val LEFT_EAR: PlatformLandmarkType?
expect val RIGHT_EAR: PlatformLandmarkType?
expect val LEFT_MOUTH: PlatformLandmarkType?
expect val RIGHT_MOUTH: PlatformLandmarkType?
expect val LEFT_SHOULDER: PlatformLandmarkType?
expect val RIGHT_SHOULDER: PlatformLandmarkType?
expect val LEFT_ELBOW: PlatformLandmarkType?
expect val RIGHT_ELBOW: PlatformLandmarkType?
expect val LEFT_WRIST: PlatformLandmarkType?
expect val RIGHT_WRIST: PlatformLandmarkType?
expect val LEFT_PINKY: PlatformLandmarkType?
expect val RIGHT_PINKY: PlatformLandmarkType?
expect val LEFT_INDEX: PlatformLandmarkType?
expect val RIGHT_INDEX: PlatformLandmarkType?
expect val LEFT_THUMB: PlatformLandmarkType?
expect val RIGHT_THUMB: PlatformLandmarkType?
expect val LEFT_HIP: PlatformLandmarkType?
expect val RIGHT_HIP: PlatformLandmarkType?
expect val LEFT_KNEE: PlatformLandmarkType?
expect val RIGHT_KNEE: PlatformLandmarkType?
expect val LEFT_ANKLE: PlatformLandmarkType?
expect val RIGHT_ANKLE: PlatformLandmarkType?
expect val LEFT_HEEL: PlatformLandmarkType?
expect val RIGHT_HEEL: PlatformLandmarkType?
expect val LEFT_FOOT_INDEX: PlatformLandmarkType?
expect val RIGHT_FOOT_INDEX: PlatformLandmarkType?

expect fun PlatformLandmark.float3DPose(): Float3Dimension
expect val PlatformLandmark.type: PlatformLandmarkType