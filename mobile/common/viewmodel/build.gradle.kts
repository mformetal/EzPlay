import com.android.build.gradle.LibraryExtension

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.lib)
}

kotlin {
    androidTarget {
        compilations.configureEach {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }

        configure<LibraryExtension> {
            namespace = "metal.ezplay.viewmodel"
            compileSdk = libs.versions.android.compileSdk.get().toInt()

            sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
            sourceSets["main"].res.srcDirs("src/androidMain/res")
            sourceSets["main"].resources.srcDirs("src/commonMain/resources")

            defaultConfig {
                minSdk = libs.versions.android.minSdk.get().toInt()
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }
        }
    }


    compilerOptions {
        // Common compiler options applied to all Kotlin source sets
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.android.ktor.okhttp)
            implementation(libs.android.viewmodel)
        }

        commonMain.dependencies {
            implementation(libs.common.ktor.core)
            implementation(libs.common.ktor.content.negotiation)
            implementation(libs.common.ktor.serialization.json)
        }
    }
}