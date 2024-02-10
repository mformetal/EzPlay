import com.android.build.gradle.LibraryExtension

plugins {
    id("kmp-library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.mobile.common.logging)
            implementation(projects.multiplatform.dto)

            implementation(libs.common.koin)
            implementation(libs.common.ktor.cio)
            implementation(libs.common.ktor.core)
            implementation(libs.common.ktor.content.negotiation)
            implementation(libs.common.ktor.logging)
            implementation(libs.common.ktor.serialization)
        }
    }
}

android {
    namespace = "metal.ezplay.network"
}