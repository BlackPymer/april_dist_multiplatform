plugins{
    `kotlin-dsl`
}

dependencies{
    implementation(libs.bundles.gradleplugins)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

gradlePlugin{
    plugins{
        // Apps plugins
        register("android.app"){
            id = "android.app"
            implementationClass = "plugins.AndroidAppPlugin"
        }
        register("desktop.app"){
            id = "desktop.app"
            implementationClass = "plugins.DesktopAppPlugin"
        }
        register("web.app"){
            id = "web.app"
            implementationClass = "plugins.WebAppPlugin"
        }
        register("ios.entry"){
            id = "ios.entry"
            implementationClass = "plugins.IOSEntryPlugin"
        }
        // Other plugins
        register("common.compose"){
            id = "common.compose"
            implementationClass = "plugins.CommonComposePlugin"
        }
        register("common.language"){
            id = "common.language"
            implementationClass = "plugins.CommonLanguagePlugin"
        }
        register("feature.plugin"){
            id = "feature.plugin"
            implementationClass = "plugins.FeaturePlugin"
        }
    }
}