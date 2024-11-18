package setups

import com.android.build.gradle.LibraryExtension
import extensions.javaVersion
import extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.get

class AndroidSetup: Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            with(pluginManager){
                apply(libs.plugins.androidLibrary.get().pluginId)
            }
            with(extensions.getByType(LibraryExtension::class.java)) {
                compileOptions {
                    compileSdk = libs.versions.compileSdk.get().toInt()

                    defaultConfig {
                        minSdk = libs.versions.minSdk.get().toInt()
                        testOptions.targetSdk = libs.versions.targetSdk.get().toInt()
                    }

                    compileOptions {
                        sourceCompatibility = project.javaVersion
                        targetCompatibility = project.javaVersion
                    }

                    sourceSets["main"].apply {
                        manifest.srcFile("src/androidMain/AndroidManifest.xml")
                        res.srcDirs("src/androidMain/resources")
                        resources.srcDirs("src/commonMain/resources")
                        kotlin.srcDirs("build/generated/ksp/**")
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
    }
}