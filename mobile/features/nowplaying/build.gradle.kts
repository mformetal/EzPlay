plugins {
    id("kmp-library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.dto)
            implementation(projects.mobile.common.network)

            implementation(libs.common.ktor.core)
            implementation(libs.common.ktor.content.negotiation)
            implementation(libs.common.ktor.serialization)
        }

        androidMain.dependencies {
            implementation(libs.android.activity)
            implementation(libs.android.activity.ktx)
            implementation(libs.android.activity.compose)
            implementation(libs.android.compose.activity)
            implementation(libs.android.compose.material3)
            implementation(libs.android.compose.material)
            implementation(libs.android.compose.material.icons)
            implementation(libs.android.compose.navigation)
            implementation(libs.android.compose.runtime)
            implementation(libs.android.activity.compose)
            implementation(libs.android.activity.compose)
            implementation(libs.android.ktor.contentnegotiation)
            implementation(libs.android.ktor.okhttp)
            implementation(libs.android.mediaplayer)

            implementation(libs.common.ktor.serialization)

            implementation(libs.koin)
            implementation(libs.koin.android)
            implementation(libs.koin.android.compose)

            implementation(projects.dto)
            implementation(projects.mobile.common.storage)
        }
    }
}

android {
    namespace = "metal.ezplay.nowplaying"
}