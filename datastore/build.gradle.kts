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
    implementation(project(":common"))
}