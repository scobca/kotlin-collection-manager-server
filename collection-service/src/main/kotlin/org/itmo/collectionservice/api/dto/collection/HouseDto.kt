package org.itmo.collectionservice.api.dto.collection

data class HouseDto(
    val id: Long,
    val name: String,
    val year: Int,
    val numberOfFloors: Long,
)
