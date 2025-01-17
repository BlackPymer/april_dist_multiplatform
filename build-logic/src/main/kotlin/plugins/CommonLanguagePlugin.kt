package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import setups.AndroidSetup
import setups.BaseLanguageSetup

class CommonLanguagePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply(BaseLanguageSetup::class)
                apply(AndroidSetup::class)
            }
            with(extensions.getByType(KotlinMultiplatformExtension::class)) {
                androidTarget()
                iosX64()
                iosArm64()
                iosSimulatorArm64()
            }
        }
    }
}