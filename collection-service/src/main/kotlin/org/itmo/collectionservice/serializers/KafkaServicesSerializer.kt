package org.itmo.collectionservice.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.itmo.collectionservice.kafka.enums.KafkaServices

object KafkaServicesSerializer : KSerializer<KafkaServices> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("KafkaServices", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: KafkaServices) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): KafkaServices {
        return KafkaServices.valueOf(decoder.decodeString())
    }
}