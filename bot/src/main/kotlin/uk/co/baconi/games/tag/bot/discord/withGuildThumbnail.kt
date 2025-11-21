package uk.co.baconi.games.tag.bot.discord

import dev.kord.core.entity.Guild
import dev.kord.rest.Image
import dev.kord.rest.builder.message.EmbedBuilder

fun EmbedBuilder.withGuildThumbnail(guild: Guild) {
    when (val cdnUrl = guild.icon?.cdnUrl) {
        null -> return
        else -> thumbnail {
            this.url = cdnUrl.toUrl()
        }
    }
}