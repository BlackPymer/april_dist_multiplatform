package extensions

import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByName
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

fun Project.commonMainDependencies(block: KotlinDependencyHandler.() -> Unit) =
    with(extensions.getByName<KotlinMultiplatformExtension>("kotlin")) {
        sourceSets.commonMain.dependencies(block)
    }

fun Project.androidMainDependencies(block: KotlinDependencyHandler.() -> Unit) =
    with(extensions.getByName<KotlinMultiplatformExtension>("kotlin")) {
        sourceSets.androidMain.dependencies(block)
    }

fun Project.iosMainDependencies(block: KotlinDependencyHandler.() -> Unit) =
    with(extensions.getByName<KotlinMultiplatformExtension>("kotlin")) {
        sourceSets.iosMain.dependencies(block)
    }

fun Project.commonTestDependencies(block: KotlinDependencyHandler.() -> Unit) =
    with(extensions.getByName<KotlinMultiplatformExtension>("kotlin")) {
        sourceSets.commonTest.dependencies(block)
    }

fun Project.androidTestDependencies(block: KotlinDependencyHandler.() -> Unit) =
    with(extensions.getByName<KotlinMultiplatformExtension>("kotlin")) {
        sourceSets.androidUnitTest.dependencies(block)
    }

fun Project.iosTestDependencies(block: KotlinDependencyHandler.() -> Unit) =
    with(extensions.getByName<KotlinMultiplatformExtension>("kotlin")) {
        sourceSets.iosTest.dependencies(block)
    }
