package extensions

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

internal val Project.kotlinMultiplatform: KotlinMultiplatformExtension
    get() = extensions.getByName("kotlin") as KotlinMultiplatformExtension

fun Project.commonMainDependencies(block: KotlinDependencyHandler.() -> Unit) =
    with(kotlinMultiplatform) {
        sourceSets.commonMain.dependencies(block)
    }

fun Project.androidMainDependencies(block: KotlinDependencyHandler.() -> Unit) =
    with(kotlinMultiplatform) {
        sourceSets.androidMain.dependencies(block)
    }

fun Project.iosMainDependencies(block: KotlinDependencyHandler.() -> Unit) =
    with(kotlinMultiplatform) {
        sourceSets.iosMain.dependencies(block)
    }

fun Project.jvmMainDependencies(block: KotlinDependencyHandler.() -> Unit) =
    with(kotlinMultiplatform) {
        sourceSets.jvmMain.dependencies(block)
    }

fun Project.jsMainDependencies(block: KotlinDependencyHandler.() -> Unit) =
    with(kotlinMultiplatform) {
        sourceSets.jsMain.dependencies(block)
    }

fun Project.commonTestDependencies(block: KotlinDependencyHandler.() -> Unit) =
    with(kotlinMultiplatform) {
        sourceSets.commonTest.dependencies(block)
    }

fun Project.androidTestDependencies(block: KotlinDependencyHandler.() -> Unit) =
    with(kotlinMultiplatform) {
        sourceSets.androidUnitTest.dependencies(block)
    }

fun Project.iosTestDependencies(block: KotlinDependencyHandler.() -> Unit) =
    with(kotlinMultiplatform) {
        sourceSets.iosTest.dependencies(block)
    }

fun Project.jvmTestDependencies(block: KotlinDependencyHandler.() -> Unit) =
    with(kotlinMultiplatform) {
        sourceSets.jvmTest.dependencies(block)
    }

fun Project.jsTestDependencies(block: KotlinDependencyHandler.() -> Unit) =
    with(kotlinMultiplatform) {
        sourceSets.jsTest.dependencies(block)
    }