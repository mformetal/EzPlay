plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleApi())

    implementation("com.android.tools.build:gradle:${libs.versions.android.plugin.get()}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
}

gradlePlugin {
    plugins {
        register("KmpLibraryPlugin") {
            id = "kmp-library"
            implementationClass = "plugins.KmpLibraryPlugin"
        }

        register("AndroidLibraryPlugin") {
            id = "android-lib"
            implementationClass = "plugins.AndroidLibraryPlugin"
        }
    }
}
