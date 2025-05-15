package org.itmo.fileservice.strategies

import org.itmo.fileservice.config.kafka.KafkaSystemProducer
import org.itmo.fileservice.kafka.dto.KafkaSystemMessageDto
import org.itmo.fileservice.kafka.enums.KafkaServices
import org.itmo.fileservice.kafka.enums.KafkaSystemThemes
import org.itmo.fileservice.utils.CollectionLoaderUtil
import org.springframework.stereotype.Service

@Service
class StartupStrategy(
    private val systemProducer: KafkaSystemProducer,
    private val collectionLoaderUtil: CollectionLoaderUtil,
) {
    fun applicationStart() {
        collectionLoaderUtil.getCollectionFromFile()

        systemProducer.sendEvent(
            KafkaSystemMessageDto(
                KafkaSystemThemes.SERVICE_STARTED,
                KafkaServices.FILE_SERVICE,
                null,
            )
        )
    }
}