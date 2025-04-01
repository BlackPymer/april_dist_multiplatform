package dev.yarobot.shirmaz.platform


@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual typealias PlatformError = Throwable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual typealias PlatformImage = com.google.mlkit.vision.common.InputImage

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual typealias PlatformLandmark = com.google.mlkit.vision.pose.PoseLandmark

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual typealias PlatformLandmarkType = Int

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ActualContext(val androidContext: android.content.Context)

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual typealias CaptureImage = androidx.camera.core.ImageCapture

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class URI(val androidUri: android.net.Uri) {
    companion object {
        fun fromAndroidUri(uri: android.net.Uri): URI {
            return URI(uri)
        }
    }
}
