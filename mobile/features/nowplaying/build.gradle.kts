plugins {
    id("kmp-library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.multiplatform.dto)
            implementation(projects.multiplatform.extensions)
            implementation(projects.mobile.common.logging)
            implementation(projects.mobile.common.network)
            implementation(projects.mobile.common.viewmodel)

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
            implementation(libs.android.compose.preview)
            implementation(libs.android.compose.tooling)
            implementation(libs.android.activity.compose)
            implementation(libs.android.koin)
            implementation(libs.android.koin.compose)
            implementation(libs.android.ktor.contentnegotiation)
            implementation(libs.android.ktor.okhttp)
            implementation(libs.android.mediaplayer)

            implementation(libs.common.koin)
            implementation(libs.common.ktor.serialization)

            implementation(projects.multiplatform.dto)
            implementation(projects.multiplatform.extensions)
            implementation(projects.mobile.android.composeResources)
            implementation(projects.mobile.android.xmlResources)
            implementation(projects.mobile.common.storage)
            implementation(projects.mobile.common.player)
        }
    }
}

android {
    namespace = "metal.ezplay.nowplaying"
}
