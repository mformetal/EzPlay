plugins {
    id("kmp-library")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.common.ktor.serialization)
        }
    }
}

android {
    namespace = "metal.ezplay.entities"
}