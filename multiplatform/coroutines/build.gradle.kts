plugins {
    id("kmp-library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.common.coroutines)
        }

        commonTest.dependencies {
            implementation(libs.testing)
            implementation(libs.testing.coroutines)
        }
    }
}

android {
    namespace = "metal.ezplay.multiplatform.coroutines"
}