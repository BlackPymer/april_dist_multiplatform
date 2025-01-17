package plugins

import extensions.commonMainDependencies
import extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import setups.AndroidSetup

class FeaturePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply(CommonComposePlugin::class)
                apply(AndroidSetup::class)
                apply(libs.plugins.kotlinCocoapods.get().pluginId)
            }
            commonMainDependencies {
                implementation(project(":core:compose"))
                implementation(project(":core:language"))
            }
        }
    }
}