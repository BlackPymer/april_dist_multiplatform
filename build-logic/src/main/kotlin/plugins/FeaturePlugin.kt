package plugins

import extensions.commonMainDependencies
import extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import setups.AndroidSetup

class FeaturePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply(CommonComposePlugin::class.java)
                apply(AndroidSetup::class.java)
                apply(libs.plugins.kotlinCocoapods.get().pluginId)
            }
            commonMainDependencies {
                implementation(project(":core:compose"))
                implementation(project(":core:language"))
            }
        }
    }
}