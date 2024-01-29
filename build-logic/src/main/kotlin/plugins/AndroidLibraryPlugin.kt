package plugins

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import extensions.catalog
import extensions.intVersion
import extensions.stringVersion
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper

class AndroidLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) = with (target) {
        apply<LibraryPlugin>()
        apply<KotlinAndroidPluginWrapper>()

        pluginManager.withPlugin("com.android.library") {
            configure<LibraryExtension> {
                compileSdk = catalog().intVersion("android.compileSdk")

                defaultConfig {
                    minSdk = catalog().intVersion("android.minSdk")
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_1_8
                    targetCompatibility = JavaVersion.VERSION_1_8
                }

                composeOptions {
                    kotlinCompilerExtensionVersion = catalog().stringVersion("composeCompiler")
                }

                buildFeatures {
                    compose = true
                }
            }
        }
    }
}