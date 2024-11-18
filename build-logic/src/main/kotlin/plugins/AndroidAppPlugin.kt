package plugins

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import extensions.androidMainDependencies
import extensions.androidTestDependencies
import extensions.applicationVersionCode
import extensions.applicationVersionName
import extensions.configureAndroid
import extensions.configureKotlinOptions
import extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import setups.BaseComposeSetup

class AndroidAppPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply(libs.plugins.androidApplication.get().pluginId)
                apply(BaseComposeSetup::class.java)
            }

            androidMainDependencies {
                implementation(project(":feature:app"))
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.activity.compose)
            }
            androidTestDependencies {
                implementation(libs.junit)
            }
            with(extensions.getByType(KotlinMultiplatformExtension::class.java)) {
                androidTarget()

                with(extensions.getByType(BaseAppModuleExtension::class.java)) {
                    defaultConfig {
                        versionCode = applicationVersionCode
                        versionName = applicationVersionName
                    }

                    packaging {
                        resources {
                            excludes += listOf(
                                "META-INF/{AL2.0,LGPL2.1}",
                                "META-INF/DEPENDENCIES",
                                "META-INF/DEPENDENCIES.txt",
                                "META-INF/LICENSE",
                                "META-INF/LICENSE.txt",
                                "META-INF/LICENSE-FIREBASE.txt",
                                "META-INF/NOTICE",
                                "META-INF/NOTICE.txt",
                                "META-INF/*.kotlin_module",
                                "META-INF/versions/9/previous-compilation-data.bin"
                            )
                        }
                    }
                }
            }
            configureKotlinOptions()


            extensions.configure<ApplicationExtension> {
                configureAndroid(this)
                defaultConfig {
                    applicationId = "dev.yarobot.shirmaz"
                    targetSdk = libs.versions.targetSdk.get().toInt()
                    versionCode = project.applicationVersionCode
                    versionName = project.applicationVersionName
                }

            }
        }
    }
}