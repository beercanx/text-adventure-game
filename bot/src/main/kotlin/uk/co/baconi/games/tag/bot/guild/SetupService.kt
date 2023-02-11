package uk.co.baconi.games.tag.bot.guild

import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.createTextChannel
import dev.kord.core.behavior.createCategory
import dev.kord.core.entity.Guild
import dev.kord.core.entity.channel.Category
import dev.kord.core.entity.channel.TextChannel
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory
import uk.co.baconi.games.tag.engine.GameEngine
import uk.co.baconi.games.tag.engine.Room

class SetupService(private val guildService: GuildService, private val gameEngine: GameEngine<Snowflake>) {

    companion object {
        const val CATEGORY_NAME = "Game"
        private val logger = LoggerFactory.getLogger(SetupService::class.java)
    }

    suspend fun setup(guild: Guild): Result<Unit> = kotlin.runCatching {
        val category = manageCategory(guild)
        val rooms = getRooms(guild)
        manageChannels(guild, category, rooms)
        removeUnaffiliatedChannels(guild, category, rooms)
        manageVerbCommands(guild)
    }

    private suspend fun manageCategory(guild: Guild): Category {
        return when (val category = guildService.findCategory(guild, CATEGORY_NAME)) {

            null -> guild.createCategory(CATEGORY_NAME).also {
                logger.debug("Category '{}' created in '{}'", it.name, guild.name)
            }

            else -> category.also {
                logger.trace("Category '{}' exists for '{}'", it.name, guild.name)
            }
        }
    }

    private fun getRooms(guild: Guild): Set<Room> {
        return gameEngine.start(guild.id).data.keys
    }

    private suspend fun manageChannels(guild: Guild, category: Category, rooms: Set<Room>): List<TextChannel> {

        val channels = category.channels
            .filterIsInstance<TextChannel>()
            .toList()

        return rooms.map { room ->

            when (val channel = channels.firstOrNull { it.name == room.displayName }) {

                null -> category.createTextChannel(room.displayName).also {
                    logger.debug("Channel '{}' created under '{}' in '{}'", it.name, category.name, guild.name)
                }

                else -> channel.also {
                    logger.trace("Channel '{}' exists under '{}' in '{}'", it.name, category.name, guild.name)
                }
            }
        }
    }

    private suspend fun removeUnaffiliatedChannels(guild: Guild, category: Category, rooms: Set<Room>) {
        // TODO
    }

    private suspend fun manageVerbCommands(guild: Guild) {
        // TODO
    }
}