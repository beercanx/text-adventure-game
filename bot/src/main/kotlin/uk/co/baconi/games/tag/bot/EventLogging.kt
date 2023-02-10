package uk.co.baconi.games.tag.bot

import dev.kord.core.Kord
import dev.kord.core.event.Event
import dev.kord.core.event.channel.*
import dev.kord.core.event.gateway.DisconnectEvent
import dev.kord.core.event.gateway.DisconnectEvent.DetachEvent
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.guild.GuildCreateEvent
import dev.kord.core.event.interaction.ApplicationCommandInteractionCreateEvent
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.event.message.MessageDeleteEvent
import dev.kord.core.event.message.MessageUpdateEvent
import dev.kord.core.event.user.VoiceStateUpdateEvent
import dev.kord.core.on
import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface EventLogging {

    val kord: Kord

    private val logger: Logger
        get() = LoggerFactory.getLogger(EventLogging::class.java)

    suspend fun initialiseEventLogging() {

        kord.on<ReadyEvent> {
            logger.info("Bot ready as '{}' for guilds {}", self.username, guildIds)
            logger.debug("Add more guilds using: https://discord.com/api/oauth2/authorize?client_id={}&permissions=16&scope=bot", self.id)
        }

        kord.on<GuildCreateEvent> {
            logger.info("Guild now available {} aka '{}'", guild.id, guild.name)
        }

        kord.on<DisconnectEvent> {
            when(this) {
                is DetachEvent -> logger.info("Detached from the gateway: shard({})", shard)
                else -> logger.error("{}", this)
            }
        }

        // Setup some trace logging, for now filtering out ones we know about
        if (logger.isTraceEnabled) kord.on<Event> {
            when (this) {
                is ReadyEvent,
                is GuildCreateEvent,
                is DisconnectEvent,
                is TypingStartEvent,
                is VoiceStateUpdateEvent,
                is MessageCreateEvent,
                is MessageUpdateEvent,
                is MessageDeleteEvent,
                is ApplicationCommandInteractionCreateEvent,
                is ChannelCreateEvent,
                is ChannelDeleteEvent,
                is ChannelUpdateEvent,
                -> return@on

                else -> logger.trace("{}", this)
            }
        }
    }
}