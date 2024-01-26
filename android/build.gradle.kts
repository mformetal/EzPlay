plugins {
    alias(libs.plugins.android.app)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "metal.ezplay.android"
    compileSdk = 34

    defaultConfig {
        applicationId = "metal.ezplay.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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
}

dependencies {
    implementation(libs.android.activity)
    implementation(libs.android.activity.ktx)
    implementation(libs.android.activity.compose)
}