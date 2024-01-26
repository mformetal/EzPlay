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

include(":dto")
include(":mobile:android")
include(":mobile:features:library")
include(":mobile:common:network")
include(":mobile:common:viewmodel")
include(":server")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
