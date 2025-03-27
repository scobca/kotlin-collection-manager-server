package org.itmo.invokerservice.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.itmo.invokerservice.kafka.enums.KafkaSystemThemes

object KafkaSystemThemesSerializer : KSerializer<KafkaSystemThemes> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("KafkaServices", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: KafkaSystemThemes) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): KafkaSystemThemes {
        return KafkaSystemThemes.valueOf(decoder.decodeString())
    }
}