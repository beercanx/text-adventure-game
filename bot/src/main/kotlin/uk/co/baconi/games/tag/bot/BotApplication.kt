package uk.co.baconi.games.tag.bot

import org.slf4j.LoggerFactory

class BotApplication {

    companion object {
        private val logger = LoggerFactory.getLogger(BotApplication::class.java)
    }

    fun start() {
        logger.info("Hello, World!")
    }
}