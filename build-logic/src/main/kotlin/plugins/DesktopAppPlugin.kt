package plugins

import extensions.compose
import extensions.desktop
import extensions.desktopMainDependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import setups.BaseComposeSetup

class DesktopAppPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        with(project){
            with(pluginManager){
                apply(BaseComposeSetup::class.java)
            }
            with(extensions.getByType(KotlinMultiplatformExtension::class.java)){
                jvm{
                    withJava()
                }
                desktopMainDependencies {
                    implementation(compose.desktop.currentOs)
                    implementation(project(":feature:app"))
                }
            }
            extensions.getByType(ComposeExtension::class.java).desktop {
                application {
                    mainClass = "dev.yarobot.shirmaz.desktopapp.ShirmazDesktopKt"

                    nativeDistributions {
                        targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                        packageName = "dev.yarobot.shirmaz.desktopapp"
                        packageVersion = "1.0.0"
                    }
                }
            }
        }
    }
}