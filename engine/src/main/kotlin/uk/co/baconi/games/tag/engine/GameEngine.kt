package uk.co.baconi.games.tag.engine

import com.typesafe.config.ConfigFactory

class GameEngine<GameId>(private val layouts: MutableMap<GameId, Layout> = mutableMapOf()) {

    companion object {
        private val config = ConfigFactory.load().getConfig("uk.co.baconi.games.tag.engine.layout")
        private val layoutData = config.getConfig("data")
        private val loadFromConfig = config.getBoolean("enabled")
    }

    @Synchronized
    fun start(gameId: GameId): Layout {
        return layouts.computeIfAbsent(gameId) {
            if (loadFromConfig) {
                Layout.Builder.fromConfig(layoutData).build()
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