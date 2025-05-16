package org.itmo.fileservice.dto.auth

data class AuthUserDto(
    val email: String,
    var password: String?
)
