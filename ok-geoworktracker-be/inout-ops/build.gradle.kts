plugins {
    alias(libs.plugins.kotlin.jvm)
    id("build-jvm")
}

dependencies{
    implementation(libs.kotlinx.datetime)
    testImplementation(kotlin("test"))
}

