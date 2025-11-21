plugins {
    application
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
    implementation(project(":datastore"))
    implementation(project(":engine"))

    implementation(libs.kord.core)
    implementation(libs.kord.emoji)
}

application {
    mainClass.set("uk.co.baconi.games.tag.bot.MainKt")
}