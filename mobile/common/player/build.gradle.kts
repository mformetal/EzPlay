plugins {
    id("kmp-library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.dto)
            implementation(projects.mobile.common.network)
            implementation(projects.mobile.common.storage)

            implementation(libs.common.io)
            implementation(libs.common.ktor.core)
        }

        androidMain.dependencies {
            implementation(libs.android.mediaplayer)
        }
    }
}

android {
    namespace = "metal.ezplay.player"
}
