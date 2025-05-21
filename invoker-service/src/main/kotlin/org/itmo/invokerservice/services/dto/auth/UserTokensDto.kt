package org.itmo.invokerservice.services.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserTokensDto(
    val accessToken: String,
    val refreshToken: String,
)
