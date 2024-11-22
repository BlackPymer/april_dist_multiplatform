import extensions.commonMainDependencies

plugins{
    id("feature.plugin")
    id("ios.entry")
}

android{
    namespace = "dev.yarobot.shirmaz.feature.app"
}

kotlin{
    cocoapods{
        version = libs.versions.app.versionCode.get()
    }
}

commonMainDependencies {
    implementation(project(":feature:camera"))
}