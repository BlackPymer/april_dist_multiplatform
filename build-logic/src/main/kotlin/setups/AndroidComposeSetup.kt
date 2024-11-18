package setups

import extensions.commonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidComposeSetup: Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            commonExtension.apply {
                buildFeatures {
                    compose = true
                }
            }

            tasks.withType<KotlinCompile>().configureEach {
                compilerOptions {

                }
            }
        }
    }
}