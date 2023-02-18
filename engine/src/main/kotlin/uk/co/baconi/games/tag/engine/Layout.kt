package uk.co.baconi.games.tag.engine

import com.typesafe.config.Config
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig

@Serializable
data class Layout(val data: Map<Room, List<Room>>) {

    fun isConnected(current: Room, next: Room): Boolean {
        return when (val connections = data[current]) {
            null -> false
            else -> connections.contains(next)
        }
    }

    class Builder {

        @OptIn(ExperimentalSerializationApi::class)
        companion object {

            private val hocon = Hocon

            fun fromConfig(config: Config): Builder {
                return Builder().add(hocon.decodeFromConfig<Map<Room, List<Room>>>(config))
            }
        }

        private val data = mutableMapOf<Room, List<Room>>()

        fun add(room: Room, vararg connections: Room): Builder = apply {
            data[room] = listOf(*connections)
        }

        fun add(map: Map<Room, List<Room>>) = apply {
            data += map
        }

        fun build(): Layout {
            return Layout(data)
        }
    }
}