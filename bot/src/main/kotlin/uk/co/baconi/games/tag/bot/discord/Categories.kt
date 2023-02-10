package uk.co.baconi.games.tag.bot.discord

import dev.kord.core.entity.Guild
import dev.kord.core.entity.channel.Category
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapNotNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Categories {

    private val logger: Logger = LoggerFactory.getLogger(Categories::class.java)

    /**
    * @throws Throwable from retrieving the existing commands from the Discord API.
    */
    suspend fun Guild.findGuildCategory(name: String): Category? {
        return channels
            .mapNotNull { it as? Category }
            .firstOrNull { command -> command.name == name }
    }
}