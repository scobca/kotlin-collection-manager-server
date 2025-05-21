package org.itmo.collectionservice.api.dto.collection

import kotlinx.serialization.Serializable

@Serializable
data class HouseDto(
    val id: Long? = null,
    val name: String,
    val year: Int,
    val numberOfFloors: Long,
)
