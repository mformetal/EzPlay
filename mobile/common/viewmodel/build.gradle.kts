plugins {
    id("kmp-library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.common.coroutines)
        }

        androidMain.dependencies {
            implementation(libs.android.viewmodel)
        }
    }
}

android {
    namespace = "metal.ezplay.viewmodel"
}