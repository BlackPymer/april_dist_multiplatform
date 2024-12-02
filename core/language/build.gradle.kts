import extensions.commonMainDependencies

plugins {
    id("common.language")
}

android{
    namespace = "dev.yarobot.shirmaz.core.language"
}

commonMainDependencies {
    api(libs.jetbrains.viewmodel)
}