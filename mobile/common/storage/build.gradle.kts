plugins {
    id("kmp-library")
    alias(libs.plugins.sqldelight)
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("metal.ezplay.storage")
        }
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.multiplatform.dto)
            implementation(projects.mobile.common.entities)
            implementation(projects.mobile.common.network)

            implementation(libs.common.coroutines)
            implementation(libs.common.datetime)
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
            implementation(libs.android.compose.navigation)
            implementation(libs.android.compose.runtime)
            implementation(libs.android.activity.compose)
            implementation(libs.android.activity.compose)
            implementation(libs.android.koin)
            implementation(libs.android.koin.compose)
            implementation(libs.android.ktor.contentnegotiation)
            implementation(libs.android.ktor.okhttp)
            implementation(libs.android.mediaplayer)
            implementation(libs.android.sqldelight)

            implementation(libs.common.koin)
            implementation(libs.common.ktor.serialization)

            implementation(projects.multiplatform.dto)
        }
    }
}

android {
    namespace = "metal.ezplay.storage"
}