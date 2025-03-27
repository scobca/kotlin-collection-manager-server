package org.itmo.collectionservice.kafka.dto

import kotlinx.serialization.Serializable
import org.itmo.collectionservice.kafka.enums.KafkaServices
import org.itmo.collectionservice.kafka.enums.KafkaSystemThemes
import org.itmo.collectionservice.serializers.KafkaServicesSerializer
import org.itmo.collectionservice.serializers.KafkaSystemThemesSerializer

@Serializable
sealed class SerializableDto

@Serializable
data class KafkaSystemMessageDto (
    @Serializable(with = KafkaSystemThemesSerializer::class)
    val theme: KafkaSystemThemes,

    @Serializable(with = KafkaServicesSerializer::class)
    val service: KafkaServices,

    val message: String?,
) : SerializableDto()

@Serializable
data class KafkaCollectionUpdateDto (
    val message: String
) : SerializableDto()