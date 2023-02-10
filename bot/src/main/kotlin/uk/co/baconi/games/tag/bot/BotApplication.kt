package uk.co.baconi.games.tag.bot

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import uk.co.baconi.games.tag.bot.global.InfoCommand
import uk.co.baconi.games.tag.bot.guild.EndCommand
import uk.co.baconi.games.tag.bot.guild.GuildCommands
import uk.co.baconi.games.tag.bot.guild.HelpCommand
import uk.co.baconi.games.tag.bot.guild.StartCommand
import uk.co.baconi.games.tag.engine.GameEngine

class BotApplication(
    override val kord: Kord,
    override val gameEngine: GameEngine<Snowflake>,
    override val configuration: BotConfiguration,
) : EventLogging, InfoCommand, GuildCommands {

    companion object {

        private val logger = LoggerFactory.getLogger(BotApplication::class.java)

        suspend operator fun invoke() = invoke(GameEngine(), BotConfiguration())

        suspend operator fun invoke(gameEngine: GameEngine<Snowflake>, configuration: BotConfiguration): BotApplication {
            return BotApplication(Kord(configuration.token), gameEngine, configuration)
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