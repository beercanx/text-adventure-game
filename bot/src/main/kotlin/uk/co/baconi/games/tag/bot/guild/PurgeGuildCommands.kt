package uk.co.baconi.games.tag.bot.guild

import dev.kord.core.Kord
import dev.kord.core.entity.Guild
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface PurgeGuildCommands {

    val kord: Kord

    private val logger: Logger
        get() = LoggerFactory.getLogger(GuildCommands::class.java)

    suspend fun purgeAllCommandsFrom(guild: Guild) {

        logger.info("Purge all command from guild '{}'", guild.name)

        kotlin.runCatching {
            guild.getApplicationCommands().onEach { command ->
                logger.debug("Purging command '{}' from '{}'", command.name, guild.name)
                command.delete()
            }.collect()
        }.onFailure { throwable ->
            logger.error("Failed purge all commands from guild '{}'", guild.name, throwable)
        }.onSuccess {
            logger.info("Command purge complete for guild '{}'", guild.name)
        }
    }
}