package org.itmo.collectionservice.services.dto

import kotlinx.serialization.Serializable
import org.itmo.collectionservice.parser.dto.FlatDto

@Serializable
data class CollectionInfoDto(
    private val type: String,
    private val initDate: String,
    private val flats: List<FlatDto>,
    private val collectionSize: Int,
)
