package org.itmo.collectionservice.api.dto.collection

import kotlinx.serialization.Serializable

@Serializable
data class CoordinatesDto(val id: Long? = null, val x: Long, val y: Float)