package uk.co.baconi.games.tag.bot.guild

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.Guild
import dev.kord.core.entity.application.GuildChatInputCommand
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.x.emoji.Emojis
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private const val PURGE = "purge"

interface PurgeCommand {

    val kord: Kord
    val guildService: GuildService
    val purgeService: PurgeService

    private val logger: Logger
        get() = LoggerFactory.getLogger(PurgeCommand::class.java)

    suspend fun registerPurgeCommandDefinition(guild: Guild): GuildChatInputCommand {
        return guildService.createCommandDefinition(guild, PURGE, "Purge the Text Adventure Game")
    }

    suspend fun registerPurgeCommand() = kord.on<GuildChatInputCommandInteractionCreateEvent> {
        if (interaction.invokedCommandName != PURGE) return@on

        logger.info("Purging from '{}'", interaction.guild.id)

        val response = interaction.deferPublicResponse()

        purgeService.purge(interaction.guild, interaction.user)
            .onFailure { throwable ->
                logger.error("Failed to purge the game fully", throwable)
                response.respond {
                    content = "${Emojis.skullAndCrossbones} encountered a problem: ${throwable::class.java}"
                }
            }.onSuccess {
                logger.info("Purge completed for '{}'", interaction.guild.id)
                response.respond {
                    content = "${Emojis.broom} the game has now been purged."
                }
            }
    }
}