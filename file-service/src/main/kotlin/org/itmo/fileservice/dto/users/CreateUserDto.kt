package org.itmo.fileservice.dto.users

data class CreateUserDto(
    val email: String,
    var password: String?,
)
