package uk.co.baconi.games.tag.engine

class GameEngine<GameId> {

    private val layouts: MutableMap<GameId, Layout> = mutableMapOf()

    fun start(gameId: GameId): Layout {
        // TODO - Generate one?
        val layout = exampleLayout
        layouts[gameId] = layout
        return layout
    }

    fun getLayout(gameId: GameId): Layout {
        return checkNotNull(layouts[gameId]) {
            "Your trying to get a layout of a game before starting one."
        }
    }

}