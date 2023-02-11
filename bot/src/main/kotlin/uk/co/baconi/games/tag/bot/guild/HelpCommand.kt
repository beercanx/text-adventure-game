package uk.co.baconi.games.tag.bot.guild

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.Guild
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.message.create.embed
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.co.baconi.games.tag.bot.discord.withApplicationThumbnail

interface HelpCommand {

    val kord: Kord
    val guildService: GuildService

    private val logger: Logger
        get() = LoggerFactory.getLogger(HelpCommand::class.java)

    suspend fun registerHelpCommand(guild: Guild) {

        val command = guildService.createCommandDefinition(guild, "help", "Help for the Text Adventure Game")

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
                            ` /setup ` Sets up the game within the guild
                            ` /purge ` Purges the game from the guild
                        """.trimIndent()
                    }
                }
            }
        }
    }
}