package plugins


import extensions.androidMainDependencies
import extensions.compose
import extensions.kotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import setups.AndroidComposeSetup
import setups.BaseComposeSetup

class CommonComposePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply(CommonLanguagePlugin::class)
                apply(BaseComposeSetup::class)
                apply(AndroidComposeSetup::class)
            }
            with(kotlinMultiplatform) {
                androidMainDependencies {
                    implementation(compose.uiTooling)
                }
            }
        }
    }
}