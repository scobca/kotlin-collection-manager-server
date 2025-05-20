package org.itmo.fileservice.dto.collection.flat

import org.itmo.fileservice.collection.items.Furnish

data class UpdateFlatDto(
    val id: Long,
    val name: String?,
    val x: Long?,
    val y: Float?,
    val area: Long?,
    val numberOfRooms: Long?,
    val price: Long?,
    val balcony: Boolean?,
    val furnish: Furnish?,
    val houseName: String?,
    val year: Int?,
    val numberOfFloors: Long?,
)
