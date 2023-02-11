package uk.co.baconi.games.tag.engine

data class Layout(val data: Map<Room, List<Room>>) {

    fun isConnected(current: Room, next: Room): Boolean {
        return when (val connections = data[current]) {
            null -> false
            else -> connections.contains(next)
        }
    }

    class Builder {

        private val data = mutableMapOf<Room, List<Room>>()

        fun add(room: Room, vararg connections: Room): Builder = apply {
            data[room] = listOf(*connections)
        }

        // TODO - Consider support for loading from configuration file (Typesafe Config + Kotlinx Serialisation)

        fun build(): Layout {
            return Layout(data)
        }
    }
}