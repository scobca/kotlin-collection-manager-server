package org.itmo.collectionservice.controllers.dto

import org.itmo.collectionservice.api.dto.collection.GetFlatDto

data class ReplaceIfLowerDto(
    val id: Long,
    val flatDto: GetFlatDto
)