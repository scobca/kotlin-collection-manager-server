package org.itmo.collectionservice.config

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.itmo.collectionservice.kafka.dto.KafkaCommandsSynchronizationDto
import org.itmo.collectionservice.serializers.KafkaCommandsSynchronizationSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.stereotype.Service

@Configuration
class KafkaCommandsProducerConfig {
    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var kafkaServerConfig: String

    @Bean
    fun commandsSynchronizationProducerFactory(): ProducerFactory<String, KafkaCommandsSynchronizationDto> {
        val configProps = mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaServerConfig,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to KafkaCommandsSynchronizationSerializer::class.java
        )
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun kafkaCommandsSynchronizationTemplate(): KafkaTemplate<String, KafkaCommandsSynchronizationDto> {
        return KafkaTemplate(commandsSynchronizationProducerFactory())
    }
}

@Service
class KafkaCommandsSynchronizationProducer(private val kafkaTemplate: KafkaTemplate<String, KafkaCommandsSynchronizationDto>) {
    fun sendEvent(message: KafkaCommandsSynchronizationDto) {
        kafkaTemplate.send("COMMANDS_SYNCHRONIZATION", message)
    }
}