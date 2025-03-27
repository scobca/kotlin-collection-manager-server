package org.itmo.invokerservice.kafka.enums

import kotlinx.serialization.Serializable

@Serializable
enum class KafkaServices {
    INVOKER_SERVICE,
    FILE_SERVICE,
    COLLECTION_SERVICE,
}