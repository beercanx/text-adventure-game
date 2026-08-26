package uk.co.baconi.games.tag.engine

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class ItemId(val id: String)

@JvmInline
@Serializable
value class Item(val description: String)