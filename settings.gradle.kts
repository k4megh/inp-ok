pluginManagement{
    val kotlinVersion:String by settings
    val foojayResolverConventionVersion:String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
    }
    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version foojayResolverConventionVersion
    }

}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "inp-ok"

includeBuild("ok-geoworktracker-be")
includeBuild("ok-geoworktracker-tests")
includeBuild("ok-geoworktracker-libs")
