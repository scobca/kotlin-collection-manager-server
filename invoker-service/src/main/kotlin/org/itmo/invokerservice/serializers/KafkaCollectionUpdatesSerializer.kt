package org.itmo.invokerservice.serializers

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer
import org.itmo.invokerservice.kafka.dto.KafkaCollectionUpdateDto
import org.springframework.stereotype.Service

private val serializer = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
}

@Service
class KafkaCollectionUpdatesSerializer : Serializer<KafkaCollectionUpdateDto> {
    override fun serialize(topic: String, obj: KafkaCollectionUpdateDto): ByteArray {
        return serializer.encodeToString<KafkaCollectionUpdateDto>(obj).toByteArray()
    }
}

@Service
class KafkaCollectionUpdatesDeserializer : Deserializer<KafkaCollectionUpdateDto> {
    override fun deserialize(topic: String, data: ByteArray): KafkaCollectionUpdateDto {
        return serializer.decodeFromString(String(data))
    }
}