package org.itmo.fileservice.config

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.itmo.fileservice.kafka.dto.KafkaCollectionUpdateDto
import org.itmo.fileservice.serializers.KafkaCollectionUpdatesSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
class KafkaCollectionUpdatedProducerConfig {
    @Value("\${spring.kafka.bootstrap-servers}")
    lateinit var kafkaConfig: String

    @Bean
    fun newTopicProducerFactory(): ProducerFactory<String, KafkaCollectionUpdateDto> {
        val configProps = mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaConfig,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to KafkaCollectionUpdatesSerializer::class.java,
        )
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun kafkaCollectionUpdatedProducer(): KafkaTemplate<String, KafkaCollectionUpdateDto> {
        return KafkaTemplate(newTopicProducerFactory())
    }
}