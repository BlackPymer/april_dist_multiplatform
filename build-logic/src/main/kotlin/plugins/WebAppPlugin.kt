package plugins

import extensions.kotlinMultiplatform
import extensions.wasmJsMainDependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinTargetContainerWithWasmPresetFunctions
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import setups.BaseComposeSetup

class WebAppPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply(BaseComposeSetup::class.java)
            }
            @OptIn(ExperimentalWasmDsl::class)
            with(extensions.getByType(KotlinTargetContainerWithWasmPresetFunctions::class.java)) {
                wasmJs {
                    moduleName = "shirmazWebApp"
                    browser {
                        val rootDirPath = project.rootDir.path
                        val projectDirPath = project.projectDir.path
                        commonWebpackConfig {
                            outputFileName = "shirmaz.js"
                            devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                                static = (static ?: mutableListOf()).apply {
                                    // Serve sources to debug inside browser
                                    add(rootDirPath)
                                    add(projectDirPath)
                                }
                            }
                        }
                    }
                    binaries.executable()
                }
            }
            with(kotlinMultiplatform){
                wasmJsMainDependencies {
                    implementation(project(":feature:app"))
                }
            }
        }
    }
}