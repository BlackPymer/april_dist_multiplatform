package setups

import extensions.commonMainDependencies
import extensions.compose
import extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class BaseComposeSetup : Plugin<Project> {
    override fun apply(project: Project) {
        with(project){
            with(pluginManager){
                apply(BaseLanguageSetup::class.java)
                apply(libs.plugins.composeCompiler.get().pluginId)
                apply(libs.plugins.composeMultiplatform.get().pluginId)
            }
            with(extensions.getByType(KotlinMultiplatformExtension::class.java)) {

                commonMainDependencies {
                    implementation(compose.animation)
                    implementation(compose.runtime)
                    implementation(compose.foundation)
                    implementation(compose.material3)
                    implementation(compose.materialIconsExtended)
                    implementation(compose.components.resources)
                    implementation(compose.uiUtil)
                    implementation(compose.ui)
                }
            }
        }
    }
}