import extensions.androidMainDependencies

plugins {
    id("feature.plugin")
}

android{
    namespace = "dev.yarobot.shirmaz.feature.camera"
}

kotlin{
    cocoapods{
        version = libs.versions.app.versionCode.get()
    }
}



androidMainDependencies {
    implementation(libs.bundles.android.filament)
    implementation(libs.bundles.android.camera)
}