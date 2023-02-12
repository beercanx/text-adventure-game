package uk.co.baconi.games.tag.bot.guild

import dev.kord.core.behavior.GuildBehavior
import dev.kord.core.entity.User
import dev.kord.core.entity.channel.Category
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import org.slf4j.LoggerFactory
import uk.co.baconi.games.tag.bot.guild.SetupService.Companion.GAME_DISPLAY_NAME

class PurgeService(private val guildService: GuildService) {

    companion object {
        private val logger = LoggerFactory.getLogger(PurgeService::class.java)
    }

    suspend fun purge(guild: GuildBehavior, user: User): Result<Unit> = kotlin.runCatching {

        when (val category = guildService.findCategory(guild, GAME_DISPLAY_NAME)) {
            null -> logger.debug("No category found called '{}'", GAME_DISPLAY_NAME)
            else -> {
                val reason = "Purge game has been called by ${user.username}"
                removeChannels(category, reason)
                removeCategory(category, reason)
            }
        }

        removeVerbCommands(guild)
    }

    private suspend fun removeChannels(category: Category?, reason: String) {
        if (category == null) return

        category.channels
            .onEach { channel ->
                logger.debug("Removing channel '{}' in '{}' from '{}'", channel.name, category.name, category.guild.id)
                channel.delete(reason)
            }
            .collect()

        // Remove the room roles
        category.guild.roles
            .filter { role -> role.name.startsWith("${category.name}: ") }
            .onEach { role ->
                logger.debug("Removing role '{}' from '{}'", role.name, category.guild.id)
                role.delete(reason)
            }
            .collect()
    }

    private suspend fun removeCategory(category: Category?, reason: String) {
        if (category == null) return

        logger.debug("Removing category '{}' from '{}'", category.name, category.guild.id)
        category.delete(reason)

        // Remove the category role
        category.guild.roles
            .filter { role -> role.name == category.name }
            .onEach { role ->
                logger.debug("Removing role '{}' from '{}'", role.name, category.guild.id)
                role.delete(reason)
            }
            .collect()
    }

    private suspend fun removeVerbCommands(guild: GuildBehavior) {
        // TODO
    }

}