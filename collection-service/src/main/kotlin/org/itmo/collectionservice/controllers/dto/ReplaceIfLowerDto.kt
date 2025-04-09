package org.itmo.collectionservice.controllers.dto

import org.itmo.collectionservice.collection.items.Flat

data class ReplaceIfLowerDto(
    val id: Long,
    val flat: Flat
)