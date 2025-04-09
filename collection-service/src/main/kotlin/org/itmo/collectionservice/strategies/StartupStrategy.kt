package org.itmo.collectionservice.strategies

import org.itmo.collectionservice.config.KafkaSystemProducer
import org.itmo.collectionservice.kafka.dto.KafkaSystemMessageDto
import org.itmo.collectionservice.kafka.enums.KafkaServices
import org.itmo.collectionservice.kafka.enums.KafkaSystemThemes
import org.springframework.stereotype.Component

@Component
class StartupStrategy(private val systemProducer: KafkaSystemProducer) {
    fun applicationStart() {
        systemProducer.sendEvent(
            KafkaSystemMessageDto(
                KafkaSystemThemes.SERVICE_STARTED,
                KafkaServices.COLLECTION_SERVICE,
                null,
            )
        )
    }
}