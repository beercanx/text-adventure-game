package uk.co.baconi.games.tag.bot.global

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.message.create.embed
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.co.baconi.games.tag.bot.BotConfiguration
import uk.co.baconi.games.tag.bot.discord.CommandDefinitions.createGlobalCommandDefinition
import uk.co.baconi.games.tag.bot.discord.withApplicationThumbnail

interface InfoCommand {

    val kord: Kord
    val configuration: BotConfiguration

    private val logger: Logger
        get() = LoggerFactory.getLogger(InfoCommand::class.java)

    suspend fun registerInfoCommand() {

        // Register global slash command definition
        kord.createGlobalCommandDefinition("info", "Bot info for Text Adventure Game")

        // Create `/info` command listener
        kord.on<ChatInputCommandInteractionCreateEvent> {
            if (interaction.command.rootName != "info") return@on
            interaction.respondPublic {
                embed {
                    title = "Text Adventure Game: 0.0.1"
                    description = "An attempt at a text adventure game, using Discord as the GUI."
                    field {
                        inline = true
                        name = "Source"
                        value = "[GitHub](https\\://github.com/beercan1989/text-adventure-game)"
                    }
                    field {
                        inline = true
                        name = "Ping"
                        value = interaction.kord.gateway.averagePing?.toString() ?: "Unknown"
                    }
                    field {
                        inline = true
                        name = "Startup"
                        value = "<t:${configuration.startup.epochSeconds}:R>"
                    }
                    withApplicationThumbnail(kord)
                }
            }
        }
    }
}