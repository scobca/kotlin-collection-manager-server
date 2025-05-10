package org.itmo.fileservice.config

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.itmo.fileservice.kafka.dto.KafkaSystemMessageDto
import org.itmo.fileservice.serializers.KafkaSystemMessageSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.stereotype.Service

@Configuration
class KafkaSystemProducerConfig {
    @Value("\${spring.kafka.bootstrap-servers}")
    lateinit var kafkaConfig: String

    @Bean
    fun systemProducerFactory(): ProducerFactory<String, KafkaSystemMessageDto> {
        val configProps = mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaConfig,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to KafkaSystemMessageSerializer::class.java,
        )
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun kafkaSystemTemplate(): KafkaTemplate<String, KafkaSystemMessageDto> {
        return KafkaTemplate(systemProducerFactory())
    }
}

@Service
class KafkaSystemProducer(private val kafkaTemplate: KafkaTemplate<String, KafkaSystemMessageDto>) {
    fun sendEvent(message: KafkaSystemMessageDto) {
        kafkaTemplate.send("SYSTEM", message)
    }
}