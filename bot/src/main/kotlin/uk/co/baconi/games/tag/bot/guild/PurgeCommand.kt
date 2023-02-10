package uk.co.baconi.games.tag.bot.guild

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.Guild
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.x.emoji.Emojis
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.co.baconi.games.tag.bot.discord.Categories.findGuildCategory
import uk.co.baconi.games.tag.bot.discord.CommandDefinitions.createGuildCommandDefinition

interface PurgeCommand {

    val kord: Kord

    private val logger: Logger
        get() = LoggerFactory.getLogger(PurgeCommand::class.java)

    suspend fun registerPurgeCommand(guild: Guild) {

        val command = kord.createGuildCommandDefinition(guild, "purge", "Purge the Text Adventure Game")

        kord.on<GuildChatInputCommandInteractionCreateEvent> {
            if (interaction.command.rootId != command.id) return@on
            if (interaction.guildId != guild.id) return@on

            logger.info("Purging from '{}'", guild.name)

            val response = interaction.deferPublicResponse()

            kotlin.runCatching {

                val reason = "Purge game has been called by ${interaction.user.username}"

                val gameCategory = guild.findGuildCategory("Game")

                // Remove all the channels
                gameCategory
                    ?.channels
                    ?.onEach { channel ->
                        logger.debug("Removing channel '{}' in '{}' from '{}'", channel.name, gameCategory.name, guild.name)
                        channel.delete(reason)
                    }
                    ?.collect()

                // Remove the category
                gameCategory?.also { category ->
                    logger.debug("Removing category '{}' from '{}'", category.name, guild.name)
                    category.delete(reason)
                }

                // TODO - Remove any game specific verbs

            }.onFailure { throwable ->
                logger.error("Failed to purge the game fully", throwable)
                response.respond {
                    content = "${Emojis.skullAndCrossbones} encountered a problem: ${throwable::class.java}"
                }
            }.onSuccess {
                logger.info("Purge completed for '{}'", guild.name)
                response.respond {
                    content = "${Emojis.broom} the game has now been purged."
                }
            }
        }
    }
}