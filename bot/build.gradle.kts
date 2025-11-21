plugins {
    application
    id("text-adventure-game.common")
}

dependencies {
    implementation(project(":common"))
    implementation(project(":datastore"))
    implementation(project(":engine"))

    implementation("dev.kord:kord-core:0.17.0")
    implementation("dev.kord.x:emoji:0.5.0")
}

application {
    mainClass.set("uk.co.baconi.games.tag.bot.MainKt")
}