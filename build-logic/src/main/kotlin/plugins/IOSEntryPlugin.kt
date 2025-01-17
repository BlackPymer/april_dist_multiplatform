package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class IOSEntryPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        with(project.extensions.getByType(KotlinMultiplatformExtension::class)){
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