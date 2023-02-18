package uk.co.baconi.games.tag.bot.discord

import dev.kord.common.Color
import dev.kord.core.behavior.interaction.response.DeferredPublicMessageInteractionResponseBehavior
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.rest.builder.message.modify.embed
import dev.kord.x.emoji.Emojis
import io.ktor.util.*

@OptIn(InternalAPI::class)
suspend fun DeferredPublicMessageInteractionResponseBehavior.respondWithThrowableEmbed(throwable: Throwable) {
    respond {
        content = "${Emojis.skullAndCrossbones} encountered a problem"
        embed {
            title = when (throwable) {
                is Error -> "Error"
                is Exception -> "Exception"
                else -> "Throwable"
            }
            color = Color(0xff0000) // Red
            field {
                name = "Class"
                value = throwable::class.toString()
            }
            when (val cause = throwable.cause) {
                is Throwable -> field {
                    name = "Cause"
                    value = cause::class.toString()
                }
            }
            when (val rootCause = throwable.rootCause) {
                is Throwable -> field {
                    name = "Root Cause"
                    value = rootCause::class.toString()
                }
            }
        }
    }
}