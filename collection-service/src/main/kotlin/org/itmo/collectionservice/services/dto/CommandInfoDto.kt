package org.itmo.collectionservice.services.dto

import kotlinx.serialization.Serializable

@Serializable
data class CommandInfoDto(
    val endpoint: String,
    val description: String,
)
