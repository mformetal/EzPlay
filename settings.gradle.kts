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

include(":mobile:android:app")
include(":mobile:android:compose-resources")
include(":mobile:android:xml-resources")
include(":mobile:common:player")
include(":mobile:features:library")
include(":mobile:features:nowplaying")
include(":mobile:common:entities")
include(":mobile:common:logging")
include(":mobile:common:network")
include(":mobile:common:storage")
include(":multiplatform:dto")
include(":multiplatform:extensions")
include(":server")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
