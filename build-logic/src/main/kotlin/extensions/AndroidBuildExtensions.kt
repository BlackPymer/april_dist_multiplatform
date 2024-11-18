package extensions

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinOptions() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.apply{
                this@configureKotlinOptions.javaVersion
            }
            jvmTarget.set(this@configureKotlinOptions.jvmTarget)

            freeCompilerArgs.add(
                "-opt-in=" +
                        "kotlin.Experimental," +
                        "kotlinx.coroutines.ExperimentalCoroutinesApi," +
                        "kotlinx.coroutines.FlowPreview," +
                        "androidx.paging.ExperimentalPagingApi"
            )
            if (project.findProperty("composeCompilerReports") == "true") {
                freeCompilerArgs.add(
                    listOf(
                        "-P",
                        "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                                project.layout.buildDirectory + "/compose_compiler",
                    ).toString()
                )
            }
            if (project.findProperty("composeCompilerMetrics") == "true") {
                freeCompilerArgs.add(
                    listOf(
                        "-P",
                        "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                                project.layout.buildDirectory + "/compose_compiler",
                    ).toString()
                )
            }
        }
    }
}

internal fun Project.configureAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = libs.versions.compileSdk.get().toInt()

        defaultConfig {
            minSdk = libs.versions.minSdk.get().toInt()
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        buildFeatures {
            buildConfig = true
        }

        compileOptions {
            sourceCompatibility = project.javaVersion
            targetCompatibility = project.javaVersion
        }

        buildTypes {
            getByName("debug") {
                enableUnitTestCoverage = true
                enableAndroidTestCoverage = true
            }
            getByName("release") {
                enableUnitTestCoverage = false
                enableAndroidTestCoverage = false
            }
        }
    }

    dependencies {
    }
}