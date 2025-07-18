import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "uk.co.baconi.games.tag"
version = "1.0-SNAPSHOT"

kotlin {
    jvmToolchain(17)
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {

    implementation(kotlin("reflect"))

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.5")

    // Configuration
    implementation("com.typesafe:config:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-hocon:1.5.0-RC")

    // Coroutines - Aligns to the version brought in by "dev.kord:kord-core:0.8.0-M17"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    // Test definitions and running
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")

    // Assertions
    testImplementation("io.kotest:kotest-assertions-core:5.5.4")

    // Mocking
    testImplementation("io.mockk:mockk:1.13.3")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging {
        showExceptions = true
        showStandardStreams = true
        events = setOf(TestLogEvent.FAILED, TestLogEvent.SKIPPED, TestLogEvent.PASSED)
        exceptionFormat = TestExceptionFormat.FULL
    }
}
