import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinCocoapods)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ShirmazApp"
            isStatic = true
        }
    }

    cocoapods {
        summary = "Some description for a Kotlin/Native module"
        homepage = "Link to a Kotlin/Native module homepage"
        version = "1.0"
        ios.deploymentTarget = "15.5"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "ShirmazApp"
            isStatic = true
        }

        pod("MLKitVision"){
            version = libs.versions.mlkit.pod.vision.get()
        }
        pod("MLImage"){
            version = libs.versions.mlkit.pod.image.get()
        }
        pod("MLKitCommon"){
            version = libs.versions.mlkit.pod.common.get()
        }
        pod("MLKitPoseDetection"){
            version = libs.versions.mlkit.pod.posedetection.get()
        }
        pod("MLKitPoseDetectionCommon"){
            version = libs.versions.mlkit.pod.posedetection.get()
        }
        pod("MLKitPoseDetectionAccurate"){
            version = libs.versions.mlkit.pod.posedetection.get()
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.pose.detection.accurate)
            implementation(libs.pose.detection)
            implementation(libs.bundles.android.camera)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.jetbrains.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.moko.permissions)
        }
    }
}

android {
    namespace = "dev.yarobot.shirmaz"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "dev.yarobot.shirmaz"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

