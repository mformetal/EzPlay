plugins {
    id("kmp-library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {

        }

        androidMain.dependencies {
            implementation(libs.android.mediaplayer)
        }
    }
}

android {
    namespace = "metal.ezplay.player"
}
