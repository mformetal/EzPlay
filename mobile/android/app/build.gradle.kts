plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "metal.ezplay.android"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "metal.ezplay.android"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    buildFeatures {
        compose = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    lint {
        disable.add("UnsafeOptInUsageError")
        baseline = file("lint-baseline.xml")
    }
}

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

dependencies {
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
    implementation(libs.android.ktor.contentnegotiation)
    implementation(libs.android.ktor.okhttp)
    implementation(libs.android.mediaplayer)

    implementation(libs.common.ktor.serialization)

    implementation(libs.koin)
    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)

    implementation(projects.mobile.android.composeResources)
    implementation(projects.mobile.android.xmlResources)
    implementation(projects.dto)
    implementation(projects.mobile.common.network)
    implementation(projects.mobile.common.storage)
    implementation(projects.mobile.common.player)
    implementation(projects.mobile.features.library)
    implementation(projects.mobile.features.nowplaying)
}