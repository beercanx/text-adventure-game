package uk.co.baconi.games.tag.bot.guild

import dev.kord.core.Kord
import dev.kord.core.behavior.GuildBehavior
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Role
import dev.kord.core.entity.application.GuildChatInputCommand
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import kotlinx.coroutines.flow.first
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.co.baconi.games.tag.bot.guild.SetupService.Companion.GAME_DISPLAY_NAME
import uk.co.baconi.games.tag.engine.Room

private const val JOIN = "join"

interface JoinCommand {

    val kord: Kord
    val guildService: GuildService

    private val logger: Logger
        get() = LoggerFactory.getLogger(JoinCommand::class.java)

    suspend fun registerStartCommandDefinition(guild: Guild): GuildChatInputCommand {
        return guildService.createCommandDefinition(guild, JOIN, "Join the Text Adventure Game")
    }

    suspend fun registerStartCommand() = kord.on<GuildChatInputCommandInteractionCreateEvent> {
        if (interaction.invokedCommandName != JOIN) return@on

        val response = interaction.deferEphemeralResponse()

        kotlin.runCatching {
            val (gameRole, firstRoomRole) = getGameRole(interaction.guild)

            if (interaction.user.roleIds.contains(gameRole.id)) {
                response.respond {
                    content = "What are you doing? You've already got access to the game!"
                }
            } else {
                val reason = "User '${interaction.user.username}' has joined the game."
                interaction.user.addRole(gameRole.id, reason)
                interaction.user.addRole(firstRoomRole.id, reason)
                // TODO - Also join the Welcome to Game channel and ping the user there and introduce them.
                response.respond {
                    content = "Welcome to the game!"
                }
            }
        }.onFailure { throwable ->
            logger.error("Failed to start the game", throwable)
            response.respond {
                content = "I don't think the game is setup, get an admin to run `/setup`."
            }
        }
    }

    private suspend fun getGameRole(guild: GuildBehavior): List<Role> {
        return listOf(
            guild.roles.first { it.name == GAME_DISPLAY_NAME },
            guild.roles.first { it.name == "${GAME_DISPLAY_NAME}: ${Room.FrontPath.displayName}" }
        )
    }
}