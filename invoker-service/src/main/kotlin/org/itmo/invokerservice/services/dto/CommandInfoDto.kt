package org.itmo.invokerservice.services.dto

import kotlinx.serialization.Serializable

@Serializable
data class CommandInfoDto(
    val endpoint: String,
    val description: String,
)
