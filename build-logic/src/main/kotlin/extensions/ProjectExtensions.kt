package extensions

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.the
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal val Project.libs: LibrariesForLibs
    get() = the<LibrariesForLibs>()

internal val Project.javaVersion: JavaVersion
    get() = JavaVersion.toVersion(libs.versions.java.get().toInt())

internal val Project.jvmTarget: JvmTarget
    get() = JvmTarget.fromTarget(this.javaVersion.majorVersion)

internal val KotlinMultiplatformExtension.compose: ComposePlugin.Dependencies
    get() = (this as ExtensionAware).extensions.getByName("compose") as ComposePlugin.Dependencies

internal fun ComposeExtension.desktop(configure: Action<DesktopExtension>): Unit =
    (this as ExtensionAware).extensions.configure("desktop", configure)

internal val Project.commonExtension: CommonExtension<*, *, *, *, *, *>
    get() = (extensions.findByType<LibraryExtension>()
        ?: extensions.findByType<ApplicationExtension>()
            ) as CommonExtension<*, *, *, *, *, *>


val Project.applicationVersionCode: Int
    get() = 1

val Project.applicationVersionName: String
    get() = "1.0"