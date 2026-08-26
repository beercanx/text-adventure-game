package uk.co.baconi.games.tag.engine

import com.typesafe.config.ConfigFactory
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.slf4j.LoggerFactory

class GameEngine<GameId>(private val layouts: MutableMap<GameId, Layout> = mutableMapOf()) {

    private val mutex = Mutex()

    companion object {
        private val config = ConfigFactory.load().getConfig("uk.co.baconi.games.tag.engine.layout")
        private val layout = Layout.fromConfig(config.getConfig("rooms"))

        private val logger = LoggerFactory.getLogger(GameEngine::class.java)
    }

    suspend fun start(gameId: GameId): Layout = mutex.withLock {
        logger.info("Starting game for '{}'", gameId)
        layouts.computeIfAbsent(gameId) {
            logger.debug("Generating layout for '{}'", gameId)
            layout.copy()
        }
    }

    suspend fun end(gameId: GameId): Unit = mutex.withLock {
        logger.info("Ending game for '{}'", gameId)
        layouts.remove(gameId)
    }

    suspend fun getLayout(gameId: GameId): Layout = mutex.withLock {
        logger.debug("Getting layout for '{}'", gameId)
        return checkNotNull(layouts[gameId]) {
            "Your trying to get a layout of a game before starting one."
        }
    }

}