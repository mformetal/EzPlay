plugins {
    id("kmp-library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
        }

        androidMain.dependencies {
            implementation(libs.android.viewmodel)
        }
    }
}

android {
    namespace = "metal.ezplay.viewmodel"
}