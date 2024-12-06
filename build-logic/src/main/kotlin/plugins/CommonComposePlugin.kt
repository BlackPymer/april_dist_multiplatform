package plugins

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
        }
    }
}