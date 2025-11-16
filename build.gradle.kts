
plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "com.otus.otuskotlin.marketplace"
version = "0.0.1"

repositories {
    mavenCentral()
    //gradlePluginPortal()
}


subprojects {
    repositories {
        mavenCentral()
     }
    group = rootProject.group
    version = rootProject.version
}


tasks {
    register("clean") {
        group = "build"
        gradle.includedBuilds.forEach {
            dependsOn(it.task(":clean"))
        }

    }

    val buildImages = register("buildImages") {
        dependsOn(gradle.includedBuild("ok-geoworktracker-be").task(":buildImages"))
    }

    val e2eTests = register("e2eTests") {
        dependsOn(buildImages)
        dependsOn(gradle.includedBuild("ok-geoworktracker-tests").task(":e2eTests"))
        mustRunAfter(buildImages)
    }

    register("check" ) {
        group = "verification"
        dependsOn(gradle.includedBuild("ok-geoworktracker-be").task(":check"))
        dependsOn(e2eTests)
    }
}
