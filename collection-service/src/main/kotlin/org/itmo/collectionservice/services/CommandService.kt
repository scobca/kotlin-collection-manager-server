package org.itmo.collectionservice.services

import org.itmo.collectionservice.collection.Collection
import org.itmo.collectionservice.services.dto.CollectionInfoDto
import org.springframework.stereotype.Service

@Service
class CommandService(private val collection: Collection)  {
    fun info(): CollectionInfoDto {
        return CollectionInfoDto(collection.getInitDate(), collection.getFlats())
    }
}