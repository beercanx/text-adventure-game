package uk.co.baconi.games.tag.bot.guild

import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.GuildBehavior
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

    suspend fun setup(guild: GuildBehavior): Result<Unit> = kotlin.runCatching {
        val category = manageCategory(guild)
        val rooms = getRooms(guild)
        manageChannels(guild, category, rooms)
        removeUnaffiliatedChannels(guild, category, rooms)
        manageVerbCommands(guild)
    }

    private suspend fun manageCategory(guild: GuildBehavior): Category {
        return when (val category = guildService.findCategory(guild, CATEGORY_NAME)) {

            null -> guild.createCategory(CATEGORY_NAME).also {
                logger.debug("Category '{}' created in '{}'", it.name, guild.id)
            }

            else -> category.also {
                logger.trace("Category '{}' exists for '{}'", it.name, guild.id)
            }
        }
    }

    private fun getRooms(guild: GuildBehavior): Set<Room> {
        return gameEngine.start(guild.id).data.keys
    }

    private suspend fun manageChannels(guild: GuildBehavior, category: Category, rooms: Set<Room>): List<TextChannel> {

        val channels = category.channels
            .filterIsInstance<TextChannel>()
            .toList()

        return rooms.map { room ->

            when (val channel = channels.firstOrNull { it.name == room.displayName }) {

                null -> category.createTextChannel(room.displayName).also {
                    logger.debug("Channel '{}' created under '{}' in '{}'", it.name, category.name, guild.id)
                }

                else -> channel.also {
                    logger.trace("Channel '{}' exists under '{}' in '{}'", it.name, category.name, guild.id)
                }
            }
        }
    }

    private suspend fun removeUnaffiliatedChannels(guild: GuildBehavior, category: Category, rooms: Set<Room>) {
        // TODO
    }

    private suspend fun manageVerbCommands(guild: GuildBehavior) {
        // TODO
    }
}