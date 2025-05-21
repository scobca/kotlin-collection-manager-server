package org.itmo.collectionservice.api.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Long,
    val email: String,
)