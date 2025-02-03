package dev.yarobot.shirmaz.platform

import kotlinx.cinterop.ExperimentalForeignApi

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual typealias PlatformError = platform.Foundation.NSError

@OptIn(ExperimentalForeignApi::class)
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual typealias PlatformImage = cocoapods.MLKitVision.MLKCompatibleImageProtocol

@OptIn(ExperimentalForeignApi::class)
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual typealias PlatformLandmark = cocoapods.MLKitPoseDetectionCommon.MLKPoseLandmark

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual typealias PlatformLandmarkType = String
