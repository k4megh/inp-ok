plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(projects.okGeoworktrackerApiV1Jackson)
    implementation(projects.okGeoworktrackerCommon)

    testImplementation(kotlin("test-junit"))
    testImplementation(projects.okGeoworktrackerStubs)
}
