plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
}

kotlin {
    jvmToolchain(17)
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {

    api(kotlin("reflect"))

    // Logging
    api(libs.logback.classic)

    // Configuration
    api(libs.typesafe.config)
    api(libs.kotlinx.serialization.hocon)

    // Coroutines
    api(libs.kotlinx.coroutines.core)
}
