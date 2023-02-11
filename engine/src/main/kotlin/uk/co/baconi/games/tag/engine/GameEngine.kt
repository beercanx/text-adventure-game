package uk.co.baconi.games.tag.engine

class GameEngine<GameId>(private val layouts: MutableMap<GameId, Layout> = mutableMapOf()) {

    fun start(gameId: GameId): Layout {
        return layouts.computeIfAbsent(gameId) {
            exampleLayout // TODO - Generate one instead?
        }
    }

    fun getLayout(gameId: GameId): Layout {
        return checkNotNull(layouts[gameId]) {
            "Your trying to get a layout of a game before starting one."
        }
    }

}