package org.itmo.invokerservice.kafka.enums

import kotlinx.serialization.Serializable

@Serializable
enum class KafkaSystemThemes {
    SERVICE_STARTED,
    COLLECTION_READY
}