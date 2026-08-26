package uk.co.baconi.games.tag.bot.guild

import dev.kord.core.Kord
import dev.kord.core.behavior.GuildBehavior
import dev.kord.core.behavior.channel.asChannelOf
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Role
import dev.kord.core.entity.application.GuildChatInputCommand
import dev.kord.core.entity.channel.CategorizableChannel
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.string
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.co.baconi.games.tag.bot.guild.SetupService.Companion.GAME_DISPLAY_NAME
import uk.co.baconi.games.tag.bot.guild.SetupService.Companion.GAME_ENTRY_POINT
import uk.co.baconi.games.tag.engine.Room
import uk.co.baconi.games.tag.engine.Verb
import kotlin.enums.enumEntries

private const val PERFORM = "action"

interface PerformCommand {

    val kord: Kord
    val guildService: GuildService

    private val logger: Logger
        get() = LoggerFactory.getLogger(JoinCommand::class.java)

    suspend fun registerActionCommandDefinition(guild: Guild): GuildChatInputCommand {
        return guildService.createCommandDefinition(guild, PERFORM, "Perform an action in Text Adventure Game") {
            string("verb", "The action to perform") {
                required = true
                for (verb in enumEntries<Verb>()) {
                    choice(verb.name, verb.name)
                }
            }
            string("item", "The item to perform action on") {
                required = false
            }
        }
    }

    suspend fun registerActionCommand() = kord.on<GuildChatInputCommandInteractionCreateEvent> {
        if (interaction.invokedCommandName != PERFORM) return@on

        val response = interaction.deferEphemeralResponse()

        runCatching {
            if (interaction.user.roles.firstOrNull { it.name == GAME_DISPLAY_NAME } == null) { // TODO - Reduce calls
                response.respond {
                    content = "What are you doing?! You're not part of this game!"
                }
            } else if (interaction.channel.asChannelOf<TextChannel>().category?.asChannel()?.name != GAME_DISPLAY_NAME) { // TODO - Replace with ID checks, also track all valid channels via SetupService.
                response.respond {
                    content = "What are you doing?! You're not inside this game!"
                }
            } else {
                val verb = enumValueOf<Verb>(interaction.command.strings.getValue("verb"))
                val item = interaction.command.strings["item"]
                response.respond {
                    content = "So you want to perform '$verb' at/with '$item'?"
                }
            }
        }.onFailure { throwable ->
            logger.error("Failed to handle action", throwable)
            response.respond {
                content = "Well that's embarrassing its never happened to me before."
            }
        }
    }
}