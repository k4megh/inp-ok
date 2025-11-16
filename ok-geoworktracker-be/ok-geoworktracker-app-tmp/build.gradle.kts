plugins {
    id("build-jvm")
    application
}

application {
    mainClass.set("ru.otus.otuskotlin.marketplace.app.tmp.MainKt")
}

dependencies {
    implementation(project(":ok-geoworktracker-api-log1"))
    implementation("ru.otus.otuskotlin.marketplace.libs:ok-geoworktracker-lib-logging-common")
    implementation("ru.otus.otuskotlin.marketplace.libs:ok-geoworktracker-lib-logging-logback")
    implementation(project(":ok-geoworktracker-common"))

    implementation(libs.coroutines.core)
}
