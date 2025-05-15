package org.itmo.fileservice.mappers

import org.itmo.fileservice.dto.users.CreateUserDto
import org.itmo.fileservice.entities.Users
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface UsersMapper {
    fun userToDto(u: Users): CreateUserDto
    fun userFromDto(u: CreateUserDto): Users
}