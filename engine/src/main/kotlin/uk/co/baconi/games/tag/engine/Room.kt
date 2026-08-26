package uk.co.baconi.games.tag.engine

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class RoomId(val id: String)

@Serializable
data class RoomConnection(val to: RoomId, val via: ItemId, val action: Verb)

@Serializable
data class Room(val description: String, val items: Map<ItemId, Item>, val connections: List<RoomConnection>)