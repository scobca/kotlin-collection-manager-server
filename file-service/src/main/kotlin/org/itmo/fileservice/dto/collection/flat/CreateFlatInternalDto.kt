package org.itmo.fileservice.dto.collection.flat

import org.itmo.fileservice.collection.items.Furnish
import org.itmo.fileservice.entities.Coordinates
import org.itmo.fileservice.entities.Houses
import org.itmo.fileservice.entities.Users
import java.time.ZonedDateTime

data class CreateFlatInternalDto(
    val name: String,
    val coordinates: Coordinates,
    val area: Long,
    val numberOfRooms: Long,
    val price: Long,
    val balcony: Boolean,
    val furnish: Furnish,
    val house: Houses,
    val user: Users,
    val createdAt: ZonedDateTime,
)