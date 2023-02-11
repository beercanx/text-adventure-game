package uk.co.baconi.games.tag.bot.guild

import dev.kord.core.Kord
import dev.kord.core.event.guild.GuildCreateEvent
import dev.kord.core.on
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.co.baconi.games.tag.bot.BotConfiguration

interface GuildCommands : GuildPurger, HelpCommand, SetupCommand, PurgeCommand {

    override val kord: Kord
    val configuration: BotConfiguration

    private val logger: Logger
        get() = LoggerFactory.getLogger(GuildCommands::class.java)

    suspend fun registerGuildCommands() {

        registerHelpCommand()
        registerSetupCommand()
        registerPurgeCommand()

        kord.on<GuildCreateEvent> {

            logger.info("Registering command definitions for '{}' with id {}", guild.name, guild.id)

            if (configuration.purgeGuildCommands) purgeAllCommandsFrom(guild)

            registerHelpCommandDefinition(guild)
            registerSetupCommandDefinition(guild)
            registerPurgeCommandDefinition(guild)
        }
    }
}