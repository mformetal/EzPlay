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

includeBuild("build-logic")

include(":dto")
include(":mobile:android:app")
include(":mobile:android:xml-resources")
include(":mobile:features:library")
include(":mobile:features:nowplaying")
include(":mobile:common:network")
include(":server")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
