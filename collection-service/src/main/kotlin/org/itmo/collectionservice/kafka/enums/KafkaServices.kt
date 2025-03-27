package org.itmo.collectionservice.kafka.enums

import kotlinx.serialization.Serializable

@Serializable
enum class KafkaServices {
    INVOKER_SERVICE,
    FILE_SERVICE,
    COLLECTION_SERVICE,
}