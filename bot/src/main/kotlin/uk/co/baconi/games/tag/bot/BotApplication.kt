package uk.co.baconi.games.tag.bot

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.event.Event
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.guild.GuildCreateEvent
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.message.create.embed
import kotlinx.datetime.Instant
import org.slf4j.LoggerFactory

class BotApplication {

    companion object {
        private val logger = LoggerFactory.getLogger(BotApplication::class.java)
    }

    private val configuration = BotConfiguration()

    suspend fun start() {
        logger.info("Starting the Text Adventure Game Discord bot")

        val kord = Kord(configuration.token)

        kord.on<ReadyEvent> {
            logger.info("Bot ready as '{}' for guilds {}", self.username, guildIds)
            logger.debug("Add more guilds using: https://discord.com/api/oauth2/authorize?client_id={}&permissions=0&scope=bot", self.id)
        }

        kord.on<GuildCreateEvent> {
            logger.info("Guild now available {} aka '{}'", guild.id, guild.name)
        }

        if(logger.isTraceEnabled) kord.on<Event> {
            when(this) {
                is ReadyEvent, is GuildCreateEvent -> return@on
                else -> logger.trace("{}", this)
            }
        }

        // TODO - Include shutdown event (disconnect?)

        // TODO - Register global slash command definitions
        // TODO - Create `/info` command
        kord.createGlobalChatInputCommand("info", "Bot info for Text Adventure Game") // TODO - Support managing changes to this.

        // TODO - Refactor away from the main method...
        kord.on<ChatInputCommandInteractionCreateEvent> {
            if (interaction.command.rootName != "info") return@on
            val self = interaction.kord.getSelf()
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
                        value = relativeTimestampFormat(configuration.startup)
                    }
                    thumbnail {
                        url = self.avatar?.url ?: self.defaultAvatar.url
                    }
                }
            }
        }

        // TODO - Register guild slash command definitions
        // TODO - Create `/help` command
        // TODO - Create `/start` command
        // TODO - Create `/end` command

        kord.login {
            //@OptIn(PrivilegedIntent::class)
            //intents += Intent.MessageContent
        }
    }

    private fun relativeTimestampFormat(instant: Instant): String = "<t:${instant.epochSeconds}:R>"
}