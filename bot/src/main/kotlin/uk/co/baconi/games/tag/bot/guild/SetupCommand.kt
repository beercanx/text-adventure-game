package uk.co.baconi.games.tag.bot.guild

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.Guild
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.x.emoji.Emojis
import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface SetupCommand {

    val kord: Kord
    val setupService: SetupService
    val guildService: GuildService

    private val logger: Logger
        get() = LoggerFactory.getLogger(SetupCommand::class.java)

    suspend fun registerSetupCommand(guild: Guild) {

        val command = guildService.createCommandDefinition(guild, "setup", "Setup the Text Adventure Game")

        kord.on<GuildChatInputCommandInteractionCreateEvent> {
            if (interaction.command.rootId != command.id) return@on
            if (interaction.guildId != guild.id) return@on

            logger.info("Setting up for '{}'", guild.name)

            val response = interaction.deferPublicResponse()

            setupService.setup(guild)
                .onFailure { throwable ->
                    logger.error("Failed to setup the game cleanly", throwable)
                    response.respond {
                        content = "${Emojis.skullAndCrossbones} encountered a problem: ${throwable::class.java}"
                    }
                }.onSuccess {
                    logger.info("Setup complete in '{}'", guild.name)
                    response.respond {
                        content = "${Emojis.constructionSite} setup is under construction..."
                    }
                }
        }
    }
}