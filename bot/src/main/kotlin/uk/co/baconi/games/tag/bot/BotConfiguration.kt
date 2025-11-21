@file:OptIn(ExperimentalTime::class)

package uk.co.baconi.games.tag.bot

import com.typesafe.config.ConfigFactory
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.Clock

data class BotConfiguration(val token: String, val startup: Instant, val purgeGuildCommands: Boolean) {

    constructor() : this(
        token = config.getString("token"),
        startup = Clock.System.now(),
        purgeGuildCommands = config.getBoolean("purge.guild.commands")
    )

    companion object {
        private val config = ConfigFactory.load().getConfig("uk.co.baconi.games.tag.bot")
    }
}