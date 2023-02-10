package uk.co.baconi.games.tag.bot.guild

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createTextChannel
import dev.kord.core.behavior.createCategory
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.Guild
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.x.emoji.Emojis
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.toList
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.co.baconi.games.tag.bot.discord.Categories.findGuildCategory
import uk.co.baconi.games.tag.bot.discord.CommandDefinitions.createGuildCommandDefinition
import uk.co.baconi.games.tag.engine.GameEngine

// TODO - Rebrand to 'setup'
interface StartCommand {

    val kord: Kord
    val gameEngine: GameEngine<Snowflake>

    private val logger: Logger
        get() = LoggerFactory.getLogger(StartCommand::class.java)

    suspend fun registerStartCommand(guild: Guild) {

        val command = kord.createGuildCommandDefinition(guild, "start", "Start the Text Adventure Game")

        kord.on<GuildChatInputCommandInteractionCreateEvent> {
            if (interaction.command.rootId != command.id) return@on
            if (interaction.guildId != guild.id) return@on
            val response = interaction.deferPublicResponse()

            kotlin.runCatching {

                val gameCategory = when(val category = guild.findGuildCategory("Game")) {

                    null -> guild.createCategory("Game").also {
                        logger.debug("Category '{}' created in '{}'", it.name, guild.name)
                    }

                    else -> category.also {
                        logger.trace("Category '{}' already exists for '{}'", it.name, guild.name)
                    }
                }

                val gameChannels = gameCategory.channels.mapNotNull { it as? TextChannel }.toList()

                val gameLayout = gameEngine.start(guild.id)

                gameLayout.data.keys.forEach { room ->

                    when (val channel = gameChannels.firstOrNull { it.name == room.displayName }) {

                        null -> gameCategory.createTextChannel(room.displayName).also {
                            logger.debug("Channel '{}' created under '{}' in '{}'", it.name, gameCategory.name, guild.name)
                        }

                        else -> channel.also {
                            logger.trace("Channel '{}' already exists for '{}' in '{}'", it.name, gameCategory.name, guild.name)
                        }
                    }
                }

                // TODO - Remove unaffiliated channels

            }.onFailure { throwable ->
                logger.error("Failed to start the game cleanly", throwable)
                response.respond {
                    content = "${Emojis.skullAndCrossbones} encountered a problem: ${throwable::class.java}"
                }
            }.onSuccess {
                response.respond {
                    content = "${Emojis.constructionSite} start is under construction..."
                }
            }
        }
    }
}