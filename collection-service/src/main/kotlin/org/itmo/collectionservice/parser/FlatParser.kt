package org.itmo.collectionservice.parser

import org.itmo.collectionservice.api.dto.collection.toSerializable
import org.itmo.collectionservice.collection.Collection
import org.itmo.collectionservice.parser.dto.FlatDto
import org.springframework.stereotype.Service

@Service
class FlatParser(private val collection: Collection) {
    fun prepareCollectionForSending(): List<FlatDto> {
        val flats = collection.getFlats()
        return flats.values.map { flat -> flat.toSerializable() }
    }
}