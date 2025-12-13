import io.ktor.plugin.features.*
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink


plugins {
    alias(libs.plugins.kotlinx.serialization)
    id("build-kmp")
//    id("io.ktor.plugin")
    alias(libs.plugins.ktor)
    //alias(libs.plugins.muschko.remote)
    id("build-docker")
 }

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

//ktor {
//    configureNativeImage(project)
//    docker {
//        localImageName.set("${project.name}-jvm")
//        imageTag.set(project.version.toString())
//        jreVersion.set(JavaVersion.toVersion(libs.versions.jvm.language.get()))
//    }
//}

jib {
    container.mainClass = application.mainClass.get()
}
docker {
    buildContext = project.layout.buildDirectory.dir("docker-x64").get().toString()
    imageName = "${project.name}-x64"
    dockerFile = "Dockerfile"
    imageTag = "${project.version}"
}

kotlin {
    // !!! Обязательно. Иначе не проходит сборка толстых джанриков в shadowJar
    jvm {withJava()}
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries {
            executable {
                entryPoint = "ru.otus.otuskotlin.marketplace.app.ktor.main"
            }
        }
    }

    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.RequiresOptIn")
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.cio)
                implementation(libs.ktor.server.cors)
                implementation(libs.ktor.server.yaml)
                implementation(libs.ktor.server.negotiation)
                implementation(libs.ktor.server.headers.response)
                implementation(libs.ktor.server.headers.caching)
                implementation(libs.ktor.server.websocket)

//                // Для того, чтоб получать содержимое запроса более одного раза
//                В Application.main добавить `install(DoubleReceive)`
//                implementation("io.ktor:ktor-server-double-receive:${libs.versions.ktor.get()}")

                implementation(project(":ok-geoworktracker-common"))
                implementation(project(":ok-geoworktracker-app-common"))
                implementation(project(":ok-geoworktracker-biz"))

                // v2 api
                implementation(project(":ok-geoworktracker-api-v2-kmp"))

                // Stubs
                implementation(project(":ok-geoworktracker-stubs"))

                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.serialization.json)

                // logging
                implementation(project(":ok-geoworktracker-api-log1"))
                implementation("ru.otus.otuskotlin.marketplace.libs:ok-geoworktracker-lib-logging-common")
                implementation("ru.otus.otuskotlin.marketplace.libs:ok-geoworktracker-lib-logging-kermit")
                implementation(libs.gwtr.logs.socket)
                
                // DB
                implementation(libs.uuid)
                implementation(projects.okGeoworktrackerRepoCommon)
                implementation(projects.okGeoworktrackerRepoStubs)
                implementation(projects.okGeoworktrackerRepoInmemory)

                // States
                implementation(libs.gwtr.state.common)
                implementation(libs.gwtr.state.biz)

            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation(libs.ktor.server.test)
                implementation(libs.ktor.client.negotiation)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))

                // jackson
                implementation(libs.ktor.serialization.jackson)
                implementation(libs.ktor.server.calllogging)
                implementation(libs.ktor.server.headers.default)

                implementation(libs.logback)

                // transport models
                implementation(projects.okGeoworktrackerApiV1Jackson)
                implementation(projects.okGeoworktrackerApiV1Mappers)
                implementation(projects.okGeoworktrackerApiV2Kmp)

                implementation("ru.otus.otuskotlin.marketplace.libs:ok-geoworktracker-lib-logging-logback")
                implementation(projects.okGeoworktrackerRepoCassandra)
 
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(libs.testcontainers.core)
                implementation(libs.logback)
                implementation(kotlin("test"))
                //implementation("org.testcontainers:testcontainers:1.19.5")
                implementation("org.testcontainers:junit-jupiter:1.19.5")
                //implementation("ch.qos.logback:logback-classic:1.4.11")
            }
        }
    }
}

tasks {
    shadowJar {
        isZip64 = true
    }
    distTar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    distZip {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    // Если ошибка: "Entry application.yaml is a duplicate but no duplicate handling strategy has been set."
    // Возникает из-за наличия файлов как в common, так и в jvm платформе
    withType(ProcessResources::class) {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    val linkReleaseExecutableLinuxX64 by getting(KotlinNativeLink::class)
    val nativeFileX64 = linkReleaseExecutableLinuxX64.binary.outputFile
    val linuxX64ProcessResources by getting(ProcessResources::class)
    dockerBuild {
        dependsOn(linkReleaseExecutableLinuxX64)
        dependsOn(linuxX64ProcessResources)
        group = "docker"
        doFirst {
            copy {
                from("Dockerfile") //.rename { "Dockerfile" }
                from(nativeFileX64)
                from(linuxX64ProcessResources.destinationDir)
                println("BUILD CONTEXT: ${buildContext.get()}")
                into(buildContext)
            }
        }
    }
}
ktor {
    configureNativeImage(project)
    docker {
        localImageName.set("${project.name}-jvm")
        imageTag.set(project.version.toString())
        jreVersion.set(JavaVersion.toVersion(libs.versions.jvm.language.get()))
    }
}
