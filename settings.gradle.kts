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
rootProject.name = "inp-ok"
include("m1-init")
