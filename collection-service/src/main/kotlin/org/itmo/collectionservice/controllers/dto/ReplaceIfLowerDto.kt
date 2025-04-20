package org.itmo.collectionservice.controllers.dto

import org.itmo.collectionservice.parser.dto.FlatDto

data class ReplaceIfLowerDto(
    val id: Long,
    val flatDto: FlatDto
)