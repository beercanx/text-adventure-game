package uk.co.baconi.games.tag.bot.guild

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.Guild
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.x.emoji.Emojis
import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface PurgeCommand {

    val kord: Kord
    val guildService: GuildService
    val purgeService: PurgeService

    private val logger: Logger
        get() = LoggerFactory.getLogger(PurgeCommand::class.java)

    suspend fun registerPurgeCommand(guild: Guild) {

        val command = guildService.createCommandDefinition(guild, "purge", "Purge the Text Adventure Game")

        kord.on<GuildChatInputCommandInteractionCreateEvent> {
            if (interaction.command.rootId != command.id) return@on
            if (interaction.guildId != guild.id) return@on

            logger.info("Purging from '{}'", guild.name)

            val response = interaction.deferPublicResponse()

            purgeService.purge(guild, interaction.user)
                .onFailure { throwable ->
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