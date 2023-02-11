package uk.co.baconi.games.tag.bot.guild

import dev.kord.core.Kord
import dev.kord.core.entity.Guild
import dev.kord.core.entity.application.GuildChatInputCommand
import dev.kord.core.entity.channel.Category
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import org.slf4j.LoggerFactory

class GuildService(private val kord: Kord) {

    companion object {
        private val logger = LoggerFactory.getLogger(GuildService::class.java)
    }

    /**
     * @throws Throwable from retrieving the existing commands from the Discord API.
     */
    suspend fun findCategory(guild: Guild, name: String): Category? {
        return guild.channels
            .filterIsInstance<Category>()
            .firstOrNull { command -> command.name == name }
    }

    /**
     * @throws Throwable from retrieving the existing commands from the Discord API.
     */
    suspend fun findCommandDefinition(guild: Guild, name: String): GuildChatInputCommand? {
        return kord.getGuildApplicationCommands(guild.id)
            .filterIsInstance<GuildChatInputCommand>()
            .firstOrNull { command -> command.name == name }
    }

    suspend fun createCommandDefinition(guild: Guild, name: String, description: String): GuildChatInputCommand {
        val definition = findCommandDefinition(guild, name)
        return when {

            definition == null -> {
                kord.createGuildChatInputCommand(guild.id, name, description).also {
                    logger.debug("Guild command '{}' definition created in '{}'", name, guild.name)
                }
            }

            definition.data.description != description -> {
                logger.warn("Guild command '{}' in '{}' has a misconfigured description", name, guild.name)
                definition
            }

            else -> {
                logger.trace("Guild command '{}' already exists for '{}'", name, guild.name)
                definition
            }
        }
    }
}