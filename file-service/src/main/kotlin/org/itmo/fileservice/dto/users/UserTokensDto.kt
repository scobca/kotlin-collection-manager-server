package org.itmo.fileservice.dto.users

data class UserTokensDto(
    val accessToken: String,
    val refreshToken: String,
)
