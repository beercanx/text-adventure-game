package uk.co.baconi.games.tag.bot.monitoring

import dev.kord.core.Kord
import dev.kord.core.event.Event
import dev.kord.core.event.channel.ChannelCreateEvent
import dev.kord.core.event.channel.ChannelDeleteEvent
import dev.kord.core.event.channel.ChannelUpdateEvent
import dev.kord.core.event.channel.TypingStartEvent
import dev.kord.core.event.gateway.DisconnectEvent
import dev.kord.core.event.gateway.DisconnectEvent.*
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.guild.GuildCreateEvent
import dev.kord.core.event.interaction.ApplicationCommandInteractionCreateEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.event.message.MessageDeleteEvent
import dev.kord.core.event.message.MessageUpdateEvent
import dev.kord.core.event.role.RoleCreateEvent
import dev.kord.core.event.role.RoleDeleteEvent
import dev.kord.core.event.role.RoleUpdateEvent
import dev.kord.core.event.user.VoiceStateUpdateEvent
import dev.kord.core.on
import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface EventLogging {

    val kord: Kord

    private val logger: Logger
        get() = LoggerFactory.getLogger(EventLogging::class.java)

    private val discordAuthorizeUrl: String
        get() = "https://discord.com/api/oauth2/authorize?client_id={}&permissions=268435472&scope=bot"

    suspend fun initialiseEventLogging() {

        kord.on<ReadyEvent> {
            logger.info("Bot ready as '{}' for guilds {}", self.username, guildIds)
            logger.debug(discordAuthorizeUrl, self.id)
        }

        kord.on<GuildCreateEvent> {
            logger.info("Guild now available {} aka '{}'", guild.id, guild.name)
        }

        kord.on<DisconnectEvent> {
            when (this) {
                is DetachEvent,
                -> logger.info("{}(shard=$shard)", this::class.java.name)

                is DiscordCloseEvent,
                -> logger.error(
                    "{}(shard=$shard, closeCode=$closeCode, recoverable=$recoverable)",
                    this::class.java.name
                )

                is ReconnectingEvent,
                is RetryLimitReachedEvent,
                is SessionReset,
                is TimeoutEvent,
                is UserCloseEvent,
                is ZombieConnectionEvent,
                -> logger.error("{}(shard=$shard)", this::class.java.name)
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
                is RoleCreateEvent,
                is RoleUpdateEvent,
                is RoleDeleteEvent,
                -> return@on

                else -> logger.trace("{}", this)
            }
        }
    }
}