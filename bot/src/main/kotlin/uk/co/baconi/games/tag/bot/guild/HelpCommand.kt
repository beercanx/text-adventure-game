package uk.co.baconi.games.tag.bot.guild

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.Guild
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.message.create.embed
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.co.baconi.games.tag.bot.discord.CommandDefinitions.createGuildCommandDefinition
import uk.co.baconi.games.tag.bot.discord.withApplicationThumbnail
import uk.co.baconi.games.tag.bot.discord.withGuildThumbnail

interface HelpCommand {

    val kord: Kord

    private val logger: Logger
        get() = LoggerFactory.getLogger(HelpCommand::class.java)

    suspend fun registerHelpCommand(guild: Guild) {

        val command = kord.createGuildCommandDefinition(guild, "help", "Help for the Text Adventure Game")

        kord.on<GuildChatInputCommandInteractionCreateEvent> {
            if (interaction.command.rootId != command.id) return@on
            if (interaction.guildId != guild.id) return@on
            interaction.respondPublic {
                embed {
                    withApplicationThumbnail(kord)
                    // TODO - Make this dynamic depending on what's been setup?
                    // TODO - Change this to provide actual game help once its started?
                    field {
                        name = "Available Commands:"
                        value = """
                            ` /help  ` Displays this general help content
                            ` /info  ` Displays information about the bot
                            ` /start ` Starts a new game
                            ` /end   ` Ends the current game
                        """.trimIndent()
                    }
                }
            }
        }
    }
}