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
    }

    compilerOptions {
        // Common compiler options applied to all Kotlin source sets
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.dto)
            implementation(projects.mobile.common.network)

            implementation(libs.common.ktor.core)
            implementation(libs.common.ktor.content.negotiation)
            implementation(libs.common.ktor.serialization)
        }

        androidMain.dependencies {
            implementation(libs.android.activity)
            implementation(libs.android.activity.ktx)
            implementation(libs.android.activity.compose)
            implementation(libs.android.compose.activity)
            implementation(libs.android.compose.material3)
            implementation(libs.android.compose.material)
            implementation(libs.android.compose.navigation)
            implementation(libs.android.compose.runtime)
            implementation(libs.android.activity.compose)
            implementation(libs.android.activity.compose)
            implementation(libs.android.ktor.cio)
            implementation(libs.android.ktor.contentnegotiation)
            implementation(libs.android.ktor.okhttp)
            implementation(libs.common.ktor.serialization)

            implementation(libs.koin)
            implementation(libs.koin.android)
            implementation(libs.koin.android.compose)

            implementation(projects.dto)
        }
    }
}

android {
    namespace = "metal.ezplay.nowplaying"
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

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    buildFeatures {
        compose = true
    }
}