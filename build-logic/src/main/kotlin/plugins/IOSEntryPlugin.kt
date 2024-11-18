package plugins

import extensions.kotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project

class IOSEntryPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        with(project.kotlinMultiplatform){
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
        }
    }
}