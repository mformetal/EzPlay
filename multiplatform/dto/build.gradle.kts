plugins {
    id("kmp-library")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization)
        }

        jvm()
    }
}

android {
    namespace = "metal.ezplay.multiplatform.dto"
}