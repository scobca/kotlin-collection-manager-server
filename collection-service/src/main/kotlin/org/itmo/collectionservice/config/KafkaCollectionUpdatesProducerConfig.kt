package org.itmo.collectionservice.config

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.itmo.collectionservice.kafka.dto.KafkaCollectionUpdateDto
import org.itmo.collectionservice.serializers.KafkaCollectionUpdatesSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.stereotype.Service

@Configuration
class KafkaCollectionUpdatedProducerConfig {
    @Bean
    fun newTopicProducerFactory(): ProducerFactory<String, KafkaCollectionUpdateDto> {
        val configProps = mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9091",
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


@Service
class KafkaCollectionUpdatesProducer(private val kafkaNewTopicTemplate: KafkaTemplate<String, KafkaCollectionUpdateDto>) {
    fun sendEvent(message: KafkaCollectionUpdateDto) {
        kafkaNewTopicTemplate.send("COLLECTION_UPDATE", message)
        println("Produced event in COLLECTION_UPDATE")
    }
}