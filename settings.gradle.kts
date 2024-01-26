rootProject.name = "EzPlay"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include(":mobile:android")
include(":mobile:common:library")
include(":mobile:common:viewmodel")
include(":server")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
