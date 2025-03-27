package org.itmo.fileservice.kafka.dto

import kotlinx.serialization.Serializable
import org.itmo.fileservice.kafka.enums.KafkaServices
import org.itmo.fileservice.kafka.enums.KafkaSystemThemes
import org.itmo.fileservice.serializers.KafkaServicesSerializer
import org.itmo.fileservice.serializers.KafkaSystemThemesSerializer

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