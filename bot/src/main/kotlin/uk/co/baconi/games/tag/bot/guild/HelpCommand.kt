package uk.co.baconi.games.tag.bot.guild

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.Guild
import dev.kord.core.entity.application.GuildChatInputCommand
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.message.create.embed
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.co.baconi.games.tag.bot.discord.withApplicationThumbnail

private const val HELP = "help"

interface HelpCommand {

    val kord: Kord
    val guildService: GuildService
    val helpService: HelpService

    private val logger: Logger
        get() = LoggerFactory.getLogger(HelpCommand::class.java)

    suspend fun registerHelpCommandDefinition(guild: Guild): GuildChatInputCommand {
        return guildService.createCommandDefinition(guild, HELP, "Help for the Text Adventure Game")
    }

    suspend fun registerHelpCommand() = kord.on<GuildChatInputCommandInteractionCreateEvent> {
        if (interaction.invokedCommandName != HELP) return@on

        interaction.respondPublic {
            embed {
                withApplicationThumbnail(kord)
                // TODO - Change this to provide actual game help once its started?
                field {
                    name = "Available Commands:"
                    value = helpService.helpMessage
                }
            }
        }
    }
}