plugins {
    id("android-lib")
}

android {
    namespace = "metal.ezplay.android.compose"
}

dependencies {
    implementation(libs.android.compose.runtime)
    implementation(libs.android.compose.material3)
    implementation(libs.android.compose.material)
}