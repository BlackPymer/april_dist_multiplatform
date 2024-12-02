import extensions.commonMainDependencies

plugins {
    id("common.compose")
}

android{
    namespace = "dev.yarobot.shirmaz.core.compose"
}

commonMainDependencies {
    api(libs.moko.permissions)
}