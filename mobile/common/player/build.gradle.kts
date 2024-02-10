plugins {
    id("kmp-library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.multiplatform.dto)
            implementation(projects.mobile.common.network)
            implementation(projects.mobile.common.storage)

            implementation(libs.common.io)
            implementation(libs.common.koin)
            implementation(libs.common.ktor.core)
        }

        commonTest.dependencies {
            implementation(libs.testing)
            implementation(libs.testing.coroutines)
            implementation(libs.testing.ktor.client)
            implementation(libs.common.ktor.content.negotiation)
            implementation(libs.common.ktor.serialization)
        }

        androidMain.dependencies {
            implementation(libs.android.koin)
            implementation(libs.android.mediaplayer)
        }
    }
}

android {
    namespace = "metal.ezplay.player"
}
