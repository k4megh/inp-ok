plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("ru.otus.otuskotlin.marketplace:ok-geoworktracker-api-v1-jackson")
    implementation("ru.otus.otuskotlin.marketplace:ok-geoworktracker-api-v1-mappers")
    implementation("ru.otus.otuskotlin.marketplace:ok-geoworktracker-api-v2-kmp")
    implementation("ru.otus.otuskotlin.marketplace:ok-geoworktracker-stubs")

    testImplementation(kotlin("test-junit5"))

    testImplementation(libs.logback)
    testImplementation(libs.kermit)

    testImplementation(libs.bundles.kotest)

    testImplementation(libs.testcontainers.core)
    testImplementation(libs.coroutines.core)

    testImplementation(libs.ktor.client.core)
    testImplementation(libs.ktor.client.okhttp)
}

var severity: String = "MINOR"

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
//        dependsOn(gradle.includedBuild(":ok-geoworktracker-app-ktor").task("publishImageToLocalRegistry"))
    }
}
