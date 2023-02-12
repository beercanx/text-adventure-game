package uk.co.baconi.games.tag.bot.guild

import dev.kord.common.entity.Permissions
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.Guild
import dev.kord.core.entity.application.GuildChatInputCommand
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.x.emoji.Emojis
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private const val SETUP = "setup"

interface SetupCommand {

    val kord: Kord
    val setupService: SetupService
    val guildService: GuildService

    private val logger: Logger
        get() = LoggerFactory.getLogger(SetupCommand::class.java)

    suspend fun registerSetupCommandDefinition(guild: Guild): GuildChatInputCommand {
        return guildService.createCommandDefinition(guild, SETUP, "Setup the Text Adventure Game") {
            defaultMemberPermissions = Permissions()
        }
    }

    suspend fun registerSetupCommand() = kord.on<GuildChatInputCommandInteractionCreateEvent> {
        if (interaction.invokedCommandName != SETUP) return@on

        val response = interaction.deferPublicResponse()

        logger.info("Setting up for '{}'", interaction.guild.id)

        setupService.setup(interaction.guild)
            .onFailure { throwable ->
                logger.error("Failed to setup the game cleanly", throwable)
                response.respond {
                    content = "${Emojis.skullAndCrossbones} encountered a problem: ${throwable::class.java}"
                }
            }.onSuccess {
                logger.info("Setup complete in '{}'", interaction.guild.id)
                response.respond {
                    content = """
                        ${Emojis.gameDie} the game category has been crated.
                        ${Emojis.book} the game channels have been created.
                        ${Emojis.scroll} the game roles have been created.

                        ${Emojis.arrowForward} why not join the game using `/start`?
                        
                        ${Emojis.constructionSite} more to come...
                    """.trimIndent()
                }
            }
    }
}