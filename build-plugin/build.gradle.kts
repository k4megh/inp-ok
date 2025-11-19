plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("build-jvm") {
            id = "build-jvm"
            implementationClass = "ru.otus.otuskotlin.marketplace.plugin.BuildPluginJvm"
        }
        register("build-kmp") {
            id = "build-kmp"
            implementationClass = "ru.otus.otuskotlin.marketplace.plugin.BuildPluginMultiplatform"
        }
    }
}

repositories {
    mavenCentral()
}


dependencies {
    // enable Ktlint formatting
//    add("detektPlugins", libs.plugin.detektFormatting)

    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(libs.plugin.kotlin)
//    implementation(libs.plugin.dokka)
    implementation(libs.plugin.binaryCompatibilityValidator)
//    implementation(libs.plugin.mavenPublish)
}

tasks.register<DefaultTask>("dependencyReport") {
    doLast {
        configurations.all {confit->
            if (confit.isCanBeResolved) {
                println("Configuration: ${confit.name}")
                //.resolvedDependencies.get()
                confit.dependencies.forEach { dep ->
                    //.module.group}:${dep.module.name}:${dep.module.version}")
                    println(" - ${dep.name}:${dep.version}")
                }
            }
            true
        }
    }
}