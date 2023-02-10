package uk.co.baconi.games.tag.bot.discord

import dev.kord.core.Kord
import dev.kord.rest.builder.message.EmbedBuilder

suspend fun EmbedBuilder.withApplicationThumbnail(kord: Kord) {
    when (val url = getApplicationAvatarUrl(kord)) {
        null -> return
        else -> thumbnail {
            this.url = url
        }
    }
}