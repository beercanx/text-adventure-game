plugins {
    application
    id("text-adventure-game.common")
}

dependencies {
    implementation(project(":common"))
    implementation(project(":datastore"))
    implementation(project(":engine"))
}

application {
    mainClass.set("uk.co.baconi.games.tag.bot.MainKt")
}