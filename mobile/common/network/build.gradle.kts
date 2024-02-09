import com.android.build.gradle.LibraryExtension

plugins {
    id("kmp-library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.multiplatform.dto)

            implementation(libs.common.ktor.core)
            implementation(libs.common.ktor.content.negotiation)
            implementation(libs.common.ktor.serialization)
        }
    }
}

android {
    namespace = "metal.ezplay.network"
}