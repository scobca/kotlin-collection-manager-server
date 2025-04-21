package org.itmo.invokerservice.kafka.dto

import kotlinx.serialization.Serializable
import org.itmo.invokerservice.kafka.enums.KafkaServices
import org.itmo.invokerservice.kafka.enums.KafkaSystemThemes
import org.itmo.invokerservice.serializers.KafkaServicesSerializer
import org.itmo.invokerservice.serializers.KafkaSystemThemesSerializer
import org.itmo.invokerservice.services.dto.CommandInfoDto

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

@Serializable
data class KafkaCommandsSynchronizationDto(
    val commands: List<CommandInfoDto>
) : SerializableDto()