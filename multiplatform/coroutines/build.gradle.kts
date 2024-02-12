plugins {
    id("kmp-library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.common.coroutines)
        }
    }
}

android {
    namespace = "metal.ezplay.multiplatform.coroutines"
}