package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import setups.AndroidSetup
import setups.BaseLanguageSetup

class CommonLanguagePlugin: Plugin<Project> {
    @OptIn(ExperimentalWasmDsl::class)
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply(BaseLanguageSetup::class.java)
                apply(AndroidSetup::class.java)
            }
            with(extensions.getByType(KotlinMultiplatformExtension::class.java)) {
                androidTarget()
                iosX64()
                iosArm64()
                iosSimulatorArm64()
            }
        }
    }
}