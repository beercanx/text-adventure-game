package uk.co.baconi.games.tag.engine

import com.typesafe.config.Config
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig

@Serializable
data class Layout(val data: Map<RoomId, Room>) {

    @OptIn(ExperimentalSerializationApi::class)
    companion object {
        fun fromConfig(config: Config): Layout {
            return Layout(Hocon.decodeFromConfig(config))
        }
    }
}