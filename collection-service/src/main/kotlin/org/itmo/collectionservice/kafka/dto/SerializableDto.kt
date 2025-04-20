package org.itmo.collectionservice.kafka.dto

import kotlinx.serialization.Serializable
import org.itmo.collectionservice.kafka.enums.KafkaServices
import org.itmo.collectionservice.kafka.enums.KafkaSystemThemes
import org.itmo.collectionservice.parser.dto.FlatDto
import org.itmo.collectionservice.serializers.KafkaServicesSerializer
import org.itmo.collectionservice.serializers.KafkaSystemThemesSerializer
import org.itmo.collectionservice.services.dto.CommandInfoDto

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
    val flats: List<FlatDto>,
) : SerializableDto()

@Serializable
data class KafkaCommandsSynchronizationDto(
    val commands: List<CommandInfoDto>
) : SerializableDto()