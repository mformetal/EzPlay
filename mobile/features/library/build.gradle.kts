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
            implementation(projects.mobile.common.storage)
            implementation(projects.mobile.common.viewmodel)

            implementation(libs.common.ktor.core)
            implementation(libs.common.ktor.content.negotiation)
            implementation(libs.common.ktor.serialization)
            implementation(libs.common.paging)
        }

        androidMain.dependencies {
            implementation(libs.android.activity)
            implementation(libs.android.activity.ktx)
            implementation(libs.android.activity.compose)
            implementation(libs.android.coil)
            implementation(libs.android.coil.compose)
            implementation(libs.android.compose.activity)
            implementation(libs.android.compose.material3)
            implementation(libs.android.compose.material)
            implementation(libs.android.compose.navigation)
            implementation(libs.android.compose.runtime)
            implementation(libs.android.activity.compose)
            implementation(libs.android.activity.compose)
            implementation(libs.android.ktor.contentnegotiation)
            implementation(libs.android.ktor.okhttp)
            implementation(libs.android.paging)
            implementation(libs.common.ktor.serialization)

            implementation(libs.koin)
            implementation(libs.koin.android)
            implementation(libs.koin.android.compose)

            implementation(projects.multiplatform.dto)
            implementation(projects.mobile.android.composeResources)
            implementation(projects.mobile.android.xmlResources)
            implementation(projects.mobile.common.logging)
            implementation(projects.mobile.common.player)
            implementation(projects.mobile.features.nowplaying)
        }
    }
}

android {
    namespace = "metal.ezplay.library"
}