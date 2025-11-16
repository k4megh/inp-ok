plugins {
    id("build-kmp")
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    sourceSets {
        val coroutinesVersion: String by project
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.coroutines.core)
                implementation("ru.otus.otuskotlin.marketplace.libs:ok-geoworktracker-lib-logging-common")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        jvmTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        nativeTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
