package org.itmo.collectionservice.serializers

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer
import org.itmo.collectionservice.kafka.dto.KafkaSystemMessageDto
import org.springframework.stereotype.Service

private val serializer = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
}

@Service
class KafkaSystemMessageSerializer : Serializer<KafkaSystemMessageDto> {
    override fun serialize(topic: String, obj: KafkaSystemMessageDto): ByteArray {
        return serializer.encodeToString<KafkaSystemMessageDto>(obj).toByteArray()
    }
}

@Service
class KafkaSystemMessageDeserializer : Deserializer<KafkaSystemMessageDto> {
    override fun deserialize(topic: String, data: ByteArray): KafkaSystemMessageDto {
        return serializer.decodeFromString(String(data))
    }
}