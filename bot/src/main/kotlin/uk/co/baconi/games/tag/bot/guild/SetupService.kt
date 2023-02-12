package uk.co.baconi.games.tag.bot.guild

import dev.kord.common.entity.*
import dev.kord.common.entity.Permission.ViewChannel
import dev.kord.core.behavior.*
import dev.kord.core.behavior.channel.CategoryBehavior
import dev.kord.core.behavior.channel.createTextChannel
import dev.kord.core.entity.Role
import dev.kord.core.entity.channel.Category
import dev.kord.core.entity.channel.TextChannel
import dev.kord.rest.builder.channel.addRoleOverwrite
import dev.kord.rest.builder.role.RoleCreateBuilder
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
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

            null -> createCategory(guild, CATEGORY_NAME).also {
                logger.debug("Category '{}' created in '{}'", it.name, guild.id)
            }

            else -> category.also {
                logger.trace("Category '{}' already exists for '{}'", it.name, guild.id)
            }
        }
    }

    private suspend fun createCategory(guild: GuildBehavior, name: String): Category {

        val categoryRole = manageRole(guild, name) {
            hoist = true
        }

        return guild.createCategory(name) {
            addRoleOverwrite(guild.id) { denied = Permissions(ViewChannel) }
            addRoleOverwrite(categoryRole.id) { allowed = Permissions(ViewChannel) }
            addRoleOverwrite(getBotRole(guild).id) { allowed = Permissions(ViewChannel) }
        }
    }

    private suspend fun manageRole(guild: GuildBehavior, name: String, block: RoleCreateBuilder.() -> Unit = {}): Role {
        return when(val role = guild.roles.firstOrNull { it.name == name }) {
            null -> guild.createRole {
                this.name = name
                block()
            }.also {
                logger.debug("Role '{}' created in '{}'", it.name, guild.id)
            }
            else -> role.also {
                logger.trace("Role '{}' already exists for '{}'", it.name, guild.id)
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

                null -> createChannel(guild, category, room).also {
                    logger.debug("Channel '{}' created under '{}' in '{}'", it.name, category.name, guild.id)
                }

                else -> channel.also {
                    logger.trace("Channel '{}' already exists under '{}' in '{}'", it.name, category.name, guild.id)
                }
            }
        }
    }

    private suspend fun createChannel(guild: GuildBehavior, category: Category, room: Room): TextChannel {

        val roomRole = manageRole(guild, "${category.name}: ${room.displayName}")

        return category.createTextChannel(room.displayName) {
            addRoleOverwrite(guild.id) { denied = Permissions(ViewChannel) }
            addRoleOverwrite(roomRole.id) { allowed = Permissions(ViewChannel) }
            addRoleOverwrite(getBotRole(guild).id) { allowed = Permissions(ViewChannel) }
        }
    }

    private suspend fun removeUnaffiliatedChannels(guild: GuildBehavior, category: CategoryBehavior, rooms: Set<Room>) {
        // TODO
    }

    private suspend fun manageVerbCommands(guild: GuildBehavior) {
        // TODO
    }

    private suspend fun getBotRole(guild: GuildBehavior): RoleBehavior {
        return guild.roles.first { it.name == guild.kord.getSelf().username }
    }
}