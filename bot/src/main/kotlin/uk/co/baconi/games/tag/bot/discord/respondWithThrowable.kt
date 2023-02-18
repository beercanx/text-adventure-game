package uk.co.baconi.games.tag.bot.discord

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
            title = when(throwable) {
                is Error -> "Error"
                is Exception -> "Exception"
                else -> "Throwable"
            }
            field {
                name = "Class"
                value = throwable::class.toString()
                inline = true
            }
            field {
                name = "Cause"
                value = when(val cause = throwable.cause) {
                    null -> ""
                    else -> cause::class.toString()
                }
                inline = true
            }
            field {
                name = "Root Cause"
                value = when(val rootCause = throwable.rootCause) {
                    null -> ""
                    else -> rootCause::class.toString()
                }
            }
//            field {
//                name = "Message"
//                value = throwable.message ?: ""
//            }
        }
    }
}