package uk.co.baconi.games.tag.bot.discord

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.Guild
import dev.kord.core.entity.application.GlobalChatInputCommand
import dev.kord.core.entity.application.GuildApplicationCommand
import dev.kord.core.entity.application.GuildChatInputCommand
import kotlinx.coroutines.flow.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object CommandDefinitions {

    private val logger: Logger = LoggerFactory.getLogger(CommandDefinitions::class.java)

    /**
     * Creates a basic global command definition.
     *
     * @throws Throwable from retrieving the existing commands from the Discord API.
     */
    suspend fun Kord.createGlobalCommandDefinition(name: String, description: String): GlobalChatInputCommand {
        val definition = findGlobalCommandDefinition(name)
        return when {
            definition == null -> {
                logger.debug("Global command '{}' definition created", name)
                createGlobalChatInputCommand(name, description)
            }
            definition.data.description != description -> {
                logger.warn("Global command '{}' has a misconfigured description", name)
                definition
            }
            else -> {
                logger.trace("Global command '{}' already exists", name)
                definition
            }
        }
    }

    suspend fun Kord.createGuildCommandDefinition(guild: Guild, name: String, description: String): GuildChatInputCommand {
        val definition = findGuildCommandDefinition(guild.id, name)
        return when {
            definition == null -> {
                createGuildChatInputCommand(guild.id, name, description).also {
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

    /**
     * TODO - Refactor possibly with aid of persistence to look up by exact Snowflake ID?
     *
     * @throws Throwable from retrieving the existing commands from the Discord API.
     */
    suspend fun Kord.findGlobalCommandDefinition(name: String): GlobalChatInputCommand? {
        return getGlobalApplicationCommands()
            .mapNotNull { it as? GlobalChatInputCommand }
            .firstOrNull { command -> command.name == name }
    }


    /**
     * TODO - Refactor possibly with aid of persistence to look up by exact Snowflake ID of the command?
     *
     * @throws Throwable from retrieving the existing commands from the Discord API.
     */
    suspend fun Kord.findGuildCommandDefinition(guildId: Snowflake, name: String): GuildChatInputCommand? {
        return getGuildApplicationCommands(guildId)
            .mapNotNull { it as? GuildChatInputCommand }
            .firstOrNull { command -> command.name == name }
    }
}