package plugins

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import extensions.catalog
import extensions.intVersion
import extensions.library
import extensions.stringVersion
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.targets

class KmpLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) = with (target) {
        apply<KotlinMultiplatformPluginWrapper>()
        apply<LibraryPlugin>()
        apply<DetektPlugin>()
        detekt()

        tasks.withType<Test>().configureEach {
            testLogging {
                showStandardStreams = true
            }
        }

        configure<KotlinMultiplatformExtension> {
            androidTarget()

            jvmToolchain(catalog().intVersion("jvm"))
        }

        kotlinExtension.sourceSets.maybeCreate("androidMain").dependencies {
            implementation(catalog().library("android.compose.runtime"))
        }

        pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
            configure<KotlinMultiplatformExtension> {
                androidTarget {
                    compilations.configureEach {
                        kotlinOptions {
                            jvmTarget = catalog().stringVersion("jvm")
                        }
                    }
                }

                compilerOptions {
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }
            }
        }

        pluginManager.withPlugin("com.android.library") {
            configure<LibraryExtension> {
                compileSdk = catalog().intVersion("android.compileSdk")

                sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
                sourceSets["main"].res.srcDirs("src/androidMain/res")
                sourceSets["main"].resources.srcDirs("src/commonMain/resources")

                defaultConfig {
                    minSdk = catalog().intVersion("android.minSdk")
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }

                composeOptions {
                    kotlinCompilerExtensionVersion = catalog().stringVersion("composeCompiler")
                }

                buildFeatures {
                    compose = true
                }

                lint {
                    disable.add("UnsafeOptInUsageError")
                    baseline = file("lint-baseline.xml")
                }
            }
        }
    }

    private fun Project.detekt() {
        extensions.configure<DetektExtension> {
            allRules = true
            autoCorrect = true
            buildUponDefaultConfig = true

            config.from("${rootDir}/detekt.yml")

            toolVersion = catalog().stringVersion("detekt")
        }

        kotlinExtension.sourceSets.configureEach {
            dependencies {
                configurations.getByName("detektPlugins")
                    .dependencies.add(
                    dependencies.create("io.gitlab.arturbosch.detekt:detekt-formatting:${catalog().stringVersion("detekt")}")
                )
            }

            if (name.startsWith("android")) {
                dependencies {
                    configurations.getByName("detektPlugins")
                        .dependencies.add(
                            dependencies.create("com.twitter.compose.rules:detekt:0.0.26")
                        )
                }
            }
        }

        tasks.withType<Detekt>().configureEach {
            exclude("**/build/**")
        }

        tasks.withType<Detekt>().matching { detektTask ->
            detektTask.name == "detekt"
        }.configureEach {
            dependsOn(tasks.getByName("detektMetadataMain"))
        }
    }
}