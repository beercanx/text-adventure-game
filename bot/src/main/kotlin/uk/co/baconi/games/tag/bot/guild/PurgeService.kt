package uk.co.baconi.games.tag.bot.guild

import dev.kord.core.entity.Guild
import dev.kord.core.entity.User
import dev.kord.core.entity.channel.Category
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.slf4j.LoggerFactory
import uk.co.baconi.games.tag.bot.guild.SetupService.Companion.CATEGORY_NAME

class PurgeService(private val guildService: GuildService) {

    companion object {
        private val logger = LoggerFactory.getLogger(PurgeService::class.java)
    }

    suspend fun purge(guild: Guild, user: User): Result<Unit> = kotlin.runCatching {

        when (val category = guildService.findCategory(guild, CATEGORY_NAME)) {
            null -> logger.debug("No category found called '{}'", CATEGORY_NAME)
            else -> {
                val reason = "Purge game has been called by ${user.username}"
                removeChannels(guild, category, reason)
                removeCategory(guild, category, reason)
            }
        }

        removeVerbCommands(guild)
    }

    private suspend fun removeChannels(guild: Guild, category: Category?, reason: String) {
        if (category == null) return

        category.channels
            .onEach { channel ->
                logger.debug("Removing channel '{}' in '{}' from '{}'", channel.name, category.name, guild.name)
                channel.delete(reason)
            }
            .collect()
    }

    private suspend fun removeCategory(guild: Guild, category: Category?, reason: String) {
        if (category == null) return

        logger.debug("Removing category '{}' from '{}'", category.name, guild.name)
        category.delete(reason)
    }

    private suspend fun removeVerbCommands(guild: Guild) {
        // TODO
    }

}