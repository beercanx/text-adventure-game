package uk.co.baconi.games.tag.bot.global

import dev.kord.core.Kord
import dev.kord.core.entity.application.GlobalChatInputCommand
import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import org.slf4j.LoggerFactory
import uk.co.baconi.games.tag.bot.guild.HelpService

class GlobalService(private val kord: Kord, private val helpService: HelpService) {

    companion object {
        private val logger = LoggerFactory.getLogger(GlobalService::class.java)
    }

    /**
     * @throws Throwable from retrieving the existing commands from the Discord API.
     */
    suspend fun findCommandDefinition(name: String): GlobalChatInputCommand? {
        return kord.getGlobalApplicationCommands()
            .filterIsInstance<GlobalChatInputCommand>()
            .firstOrNull { command -> command.name == name }
    }

    /**
     * @throws Throwable from retrieving the existing commands from the Discord API.
     */
    suspend fun createCommandDefinition(
        name: String,
        description: String,
        builder: GlobalChatInputCreateBuilder.() -> Unit = {}
    ): GlobalChatInputCommand {

        val definition = findCommandDefinition(name)
        return when {

            definition == null -> {
                kord.createGlobalChatInputCommand(name, description, builder).also {
                    logger.debug("Global command '{}' definition created", name)
                }
            }

            definition.data.description != description -> {
                logger.warn("Global command '{}' has a misconfigured description", name)
                definition
            }

            else -> {
                logger.trace("Global command '{}' already exists", name)
                definition
            }
        }.also {
            helpService.registerCommand(name, description)
        }
    }
}