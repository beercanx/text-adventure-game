package uk.co.baconi.games.tag.bot.discord

import dev.kord.core.Kord
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val logger: Logger
    get() = LoggerFactory.getLogger("uk.co.baconi.games.tag.bot.discord.getBotAvatar")

// TODO - Review this later
suspend fun getApplicationAvatarUrl(kord: Kord) = runCatching {
    kord.getSelf()
}.map { self ->
    self.avatar?.cdnUrl ?: self.defaultAvatar.cdnUrl
}.onFailure { throwable ->
    logger.error("Failed to get kord.self", throwable)
}.getOrNull()
