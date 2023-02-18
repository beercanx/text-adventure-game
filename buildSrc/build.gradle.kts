plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    val kotlinVersion = "1.8.10"
    implementation(kotlin("gradle-plugin", kotlinVersion))
    implementation(kotlin("serialization", kotlinVersion))
}