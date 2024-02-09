plugins {
    id("kmp-library")
}

kotlin {
    sourceSets {
        commonTest.dependencies {
            implementation(libs.testing)
        }
    }
}

android {
    namespace = "metal.ezplay.multiplatform.extensions"
}