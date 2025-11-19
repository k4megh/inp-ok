plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}


allprojects {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

subprojects {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    group = rootProject.group
    version = rootProject.version
}

group = "ru.otus.otuskotlin.marketplace.libs"
version = "1.0-SNAPSHOT"

ext {
    val specDir = layout.projectDirectory.dir("../specs")
    set("spec-v1", specDir.file("specs-ad-v1.yaml").toString())
    set("spec-v2", specDir.file("specs-ad-v2.yaml").toString())
}

tasks {
    arrayOf("build", "clean", "check").forEach {tsk ->
        register(tsk ) {
            group = "build"
            dependsOn(subprojects.map {  it.getTasksByName(tsk,false)})
        }
    }
}
