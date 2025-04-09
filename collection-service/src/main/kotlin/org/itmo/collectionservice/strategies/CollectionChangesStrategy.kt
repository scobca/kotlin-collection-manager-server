package org.itmo.collectionservice.strategies

import org.itmo.collectionservice.config.KafkaCollectionUpdatesProducer
import org.itmo.collectionservice.kafka.dto.KafkaCollectionUpdateDto
import org.itmo.collectionservice.parser.FlatParser
import org.springframework.stereotype.Component

@Component
class CollectionChangesStrategy(
    private val flatParser: FlatParser,
    private val collectionUpdatesProducer: KafkaCollectionUpdatesProducer
) {
    fun onCollectionUpdate() {
        val jsonFlats = flatParser.prepareCollectionForSending()
        collectionUpdatesProducer.sendEvent(KafkaCollectionUpdateDto(jsonFlats))

        println("Collection updates sent.")
    }
}