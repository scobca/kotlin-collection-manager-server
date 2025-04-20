package org.itmo.invokerservice.serializers

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer
import org.itmo.invokerservice.kafka.dto.KafkaCommandsSynchronizationDto
import org.springframework.stereotype.Service
import kotlin.text.toByteArray

private val serializer = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
}

@Service
class KafkaCommandsSynchronizationSerializer : Serializer<KafkaCommandsSynchronizationDto> {
    override fun serialize(
        topic: String,
        obj: KafkaCommandsSynchronizationDto
    ): ByteArray? {
        return serializer.encodeToString<KafkaCommandsSynchronizationDto>(obj).toByteArray()
    }
}

@Service
class KafkaCommandsSynchronizationDeserializer : Deserializer<KafkaCommandsSynchronizationDto> {
    override fun deserialize(topic: String, data: ByteArray): KafkaCommandsSynchronizationDto {
        return serializer.decodeFromString<KafkaCommandsSynchronizationDto>(String(data))
    }
}