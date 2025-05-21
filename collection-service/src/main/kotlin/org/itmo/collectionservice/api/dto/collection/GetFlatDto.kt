package org.itmo.collectionservice.api.dto.collection

import org.itmo.collectionservice.api.dto.user.UserDto
import org.itmo.collectionservice.collection.items.Coordinates
import org.itmo.collectionservice.collection.items.Furnish
import java.time.ZonedDateTime

data class GetFlatDto(
    val id: Long,
    val name: String,
    val coordinates: Coordinates,
    val area: Long,
    val numberOfRooms: Long,
    val price: Long,
    val balcony: Boolean,
    val furnish: Furnish,
    val house: HouseDto,
    val user: UserDto,
    val createdAt: ZonedDateTime,
)