package uk.co.baconi.games.tag.bot.guild

import dev.kord.common.entity.Permissions
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.Guild
import dev.kord.core.entity.application.GuildChatInputCommand
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.message.embed
import dev.kord.x.emoji.Emojis
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.co.baconi.games.tag.bot.discord.respondWithThrowableEmbed

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
                response.respondWithThrowableEmbed(throwable)
            }.onSuccess {
                logger.info("Setup complete in '{}'", interaction.guild.id)
                response.respond {
                    embed {
                        field {
                            name = "Setup:"
                            value = """
                                ${Emojis.gameDie} the game category has been crated.
                                ${Emojis.book} the game channels have been created.
                                ${Emojis.scroll} the game roles have been created.
        
                                ${Emojis.arrowForward} why not join the game using `/join`?
        
                                ${Emojis.constructionSite} more to come...
                            """.trimIndent()
                        }
                    }
                }
            }
    }
}