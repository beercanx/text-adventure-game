package uk.co.baconi.games.tag.bot

import org.slf4j.LoggerFactory

class BotApplication {

    companion object {
        private val logger = LoggerFactory.getLogger(BotApplication::class.java)
    }

    // TODO - Choose Discord library
    // TODO - Choose DI library

    // TODO - Create `/info` command

    // TODO - Create `/help` command
    // TODO - Create `/start` command
    // TODO - Create `/end` command

    fun start() {
        logger.info("Starting the Text Adventure Game Discord bot.")
    }
}