package org.itmo.fileservice.config

import io.github.cdimascio.dotenv.Dotenv
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.itmo.fileservice.kafka.dto.KafkaSystemMessageDto
import org.itmo.fileservice.kafka.enums.KafkaServices
import org.itmo.fileservice.kafka.enums.KafkaSystemThemes
import org.itmo.fileservice.serializers.KafkaSystemMessageDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.stereotype.Service

@Configuration
class KafkaConsumerConfig {
    private val dotenv: Dotenv = Dotenv.load()

    @Bean
    fun kafkaSystemMessageConsumerFactory(): ConsumerFactory<String, KafkaSystemMessageDto> {
        val props = HashMap<String, Any>()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = dotenv.get("BOOTSTRAP_SERVERS_CONFIG")
        props[ConsumerConfig.GROUP_ID_CONFIG] = "FileService"
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = KafkaSystemMessageDeserializer::class.java
        return DefaultKafkaConsumerFactory(props)
    }
}

@Service
class KafkaSystemMessagesConsumer(
    private val deserializer: KafkaSystemMessageDeserializer,
    private val systemProducer: KafkaSystemProducer,
) {
    @KafkaListener(topics = ["SYSTEM"], groupId = "FileService")
    fun receiveMessage(consumerRecord: ConsumerRecord<String, String>) {
        val message = deserializer.deserialize("SYSTEM", consumerRecord.value().toString().toByteArray())

        if (message.service == KafkaServices.COLLECTION_SERVICE) {
            systemProducer.sendEvent(KafkaSystemMessageDto(
                KafkaSystemThemes.COLLECTION_READY,
                KafkaServices.FILE_SERVICE,
                "Collection ready for copying"
            ))
        }
    }
}