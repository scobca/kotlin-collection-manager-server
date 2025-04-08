package org.itmo.fileservice.parser.dto

import kotlinx.serialization.Serializable

@Serializable
data class HouseDto(
    val name: String?,
    val year: Int?,
    val numberOfFloors: Long?,
)
