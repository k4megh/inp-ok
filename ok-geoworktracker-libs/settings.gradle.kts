rootProject.name = "ok-geoworktracker-libs"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    val kotlinVersion:String by settings
    val foojayResolverConventionVersion:String by settings
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
    }
    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version foojayResolverConventionVersion
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}


include(":ok-geoworktracker-lib-logging-common")
include(":ok-geoworktracker-lib-logging-kermit")
include(":ok-geoworktracker-lib-logging-logback")
include(":ok-geoworktracker-lib-logging-socket")



