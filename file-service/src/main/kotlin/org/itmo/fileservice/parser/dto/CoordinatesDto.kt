package org.itmo.fileservice.parser.dto

import kotlinx.serialization.Serializable

@Serializable
data class CoordinatesDto(
    val x: Long?,
    val y: Float?,
)
