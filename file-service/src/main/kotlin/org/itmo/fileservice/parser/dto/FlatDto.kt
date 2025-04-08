package org.itmo.fileservice.parser.dto

import kotlinx.serialization.Serializable
import org.itmo.fileservice.collection.items.Furnish

@Serializable
data class FlatDto(
    val id: Long,
    val name: String?,
    val coordinates: CoordinatesDto?,
    val area: Long?,
    val numberOfRooms: Long?,
    val price: Long?,
    val balcony: Boolean?,
    val furnish: Furnish?,
    val house: HouseDto?,
)