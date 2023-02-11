package uk.co.baconi.games.tag.bot

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import org.slf4j.LoggerFactory
import uk.co.baconi.games.tag.bot.global.GlobalService
import uk.co.baconi.games.tag.bot.global.InfoCommand
import uk.co.baconi.games.tag.bot.guild.GuildCommands
import uk.co.baconi.games.tag.bot.guild.GuildService
import uk.co.baconi.games.tag.bot.guild.PurgeService
import uk.co.baconi.games.tag.bot.guild.SetupService
import uk.co.baconi.games.tag.bot.monitoring.EventLogging
import uk.co.baconi.games.tag.engine.GameEngine

class BotApplication(
    override val kord: Kord,
    override val configuration: BotConfiguration,
) : EventLogging, InfoCommand, GuildCommands {

    private val gameEngine = GameEngine<Snowflake>()

    override val globalService = GlobalService(kord)
    override val guildService = GuildService(kord)
    override val setupService = SetupService(guildService, gameEngine)
    override val purgeService = PurgeService(guildService)

    companion object {

        private val logger = LoggerFactory.getLogger(BotApplication::class.java)

        suspend operator fun invoke(): BotApplication = BotConfiguration().let { configuration ->
            return BotApplication(Kord(configuration.token), configuration)
        }
    }

    suspend fun start() {

        logger.info("Starting the Text Adventure Game Discord bot")

        initialiseEventLogging()

        // Register the global commands
        registerInfoCommand()

        // Register the guild commands
        registerGuildCommands()

        kord.login {
            //@OptIn(PrivilegedIntent::class)
            //intents += Intent.MessageContent
        }
    }
}