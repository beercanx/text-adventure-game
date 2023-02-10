package uk.co.baconi.games.tag.bot.guild

import dev.kord.core.Kord
import dev.kord.core.behavior.createCategory
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.Guild
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.x.emoji.Emojis
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.co.baconi.games.tag.bot.discord.Categories.findGuildCategory
import uk.co.baconi.games.tag.bot.discord.CommandDefinitions.createGuildCommandDefinition

// TODO - Rebrand to 'purge'
interface EndCommand {

    val kord: Kord

    private val logger: Logger
        get() = LoggerFactory.getLogger(EndCommand::class.java)

    suspend fun registerEndCommand(guild: Guild) {

        val command = kord.createGuildCommandDefinition(guild, "end", "End the Text Adventure Game")

        kord.on<GuildChatInputCommandInteractionCreateEvent> {
            if (interaction.command.rootId != command.id) return@on
            if (interaction.guildId != guild.id) return@on

            val response = interaction.deferPublicResponse()

            kotlin.runCatching {

                val reason = "End game has been called by ${interaction.user.username}"

                val gameCategory = guild.findGuildCategory("Game")

                // Remove all the channels
                gameCategory
                    ?.channels
                    ?.onEach { channel ->
                        logger.debug("Removing channel: {}", channel.name)
                        channel.delete(reason)
                    }
                    ?.collect()

                // Remove the category
                gameCategory
                    ?.delete(reason)

                // TODO - Remove any game specific slash commands

            }.onFailure { throwable ->
                logger.error("Failed to end the game cleanly", throwable)
                response.respond {
                    content = "${Emojis.skullAndCrossbones} encountered a problem: ${throwable::class.java}"
                }
            }.onSuccess {
                response.respond {
                    content = "${Emojis.broom} the game is over now, and all the game rooms have been removed."
                }
            }
        }
    }
}