rootProject.name = "ok-geoworktracker-be"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}


pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
        id("build-docker") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")



include(":ok-geoworktracker-api-v1-jackson")
include(":ok-geoworktracker-api-v1-mappers")
include(":ok-geoworktracker-api-v2-kmp")
include(":ok-geoworktracker-api-log1")
include(":ok-geoworktracker-common")
include(":ok-geoworktracker-stubs")
//include(":ok-geoworktracker-app-tmp")
include(":ok-geoworktracker-biz")

include(":ok-geoworktracker-app-common")
include(":ok-geoworktracker-app-ktor")