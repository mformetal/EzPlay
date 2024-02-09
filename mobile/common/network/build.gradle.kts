import com.android.build.gradle.LibraryExtension

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
}

kotlin {
    androidTarget {
        compilations.configureEach {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }

        configure<LibraryExtension> {
            namespace = "metal.ezplay.network"
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
        commonMain.dependencies {
            implementation(projects.multiplatform.dto)

            implementation(libs.common.ktor.core)
            implementation(libs.common.ktor.content.negotiation)
            implementation(libs.common.ktor.serialization)
            implementation(libs.common.ktor.websockets)
        }
    }
}