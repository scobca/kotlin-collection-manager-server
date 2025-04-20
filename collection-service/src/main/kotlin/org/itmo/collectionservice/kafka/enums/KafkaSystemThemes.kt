package org.itmo.collectionservice.kafka.enums

import kotlinx.serialization.Serializable

@Serializable
enum class KafkaSystemThemes {
    SERVICE_STARTED,
    COLLECTION_READY,
}