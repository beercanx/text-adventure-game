package uk.co.baconi.games.tag.bot.global

import dev.kord.core.Kord
import dev.kord.core.entity.application.GlobalChatInputCommand
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import org.slf4j.LoggerFactory

class GlobalService(private val kord: Kord) {

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
    suspend fun createCommandDefinition(name: String, description: String): GlobalChatInputCommand {
        val definition = findCommandDefinition(name)
        return when {

            definition == null -> {
                logger.debug("Global command '{}' definition created", name)
                kord.createGlobalChatInputCommand(name, description)
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
}