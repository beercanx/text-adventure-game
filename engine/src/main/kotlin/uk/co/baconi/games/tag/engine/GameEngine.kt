package uk.co.baconi.games.tag.engine

import com.typesafe.config.ConfigFactory
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class GameEngine<GameId>(private val layouts: MutableMap<GameId, Layout> = mutableMapOf()) {

    private val mutex = Mutex()

    companion object {
        private val config = ConfigFactory.load().getConfig("uk.co.baconi.games.tag.engine.layout")
        private val layoutData = config.getConfig("data")
        private val loadFromConfig = config.getBoolean("enabled")
    }

    suspend fun start(gameId: GameId): Layout = mutex.withLock {
        layouts.computeIfAbsent(gameId) {
            if (loadFromConfig) {
                Layout.fromConfig(layoutData)
            } else {
                exampleLayout
            }
        }
    }

    fun getLayout(gameId: GameId): Layout {
        return checkNotNull(layouts[gameId]) {
            "Your trying to get a layout of a game before starting one."
        }
    }

}