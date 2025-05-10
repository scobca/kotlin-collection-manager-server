package org.itmo.collectionservice.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.itmo.collectionservice.kafka.dto.KafkaCollectionUpdateDto
import org.itmo.collectionservice.serializers.KafkaCollectionUpdatesDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.stereotype.Service

@Configuration
class KafkaCollectionUpdatesConsumerConfig {
    @Value("\${spring.kafka.bootstrap-serverss}")
    private lateinit var kafkaServerConfig: String

    @Bean
    fun kafkaCollectionUpdatesConsumerFactory(): ConsumerFactory<String, KafkaCollectionUpdateDto> {
        val props = HashMap<String, Any>()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaServerConfig
        props[ConsumerConfig.GROUP_ID_CONFIG] = "CollectionService"
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = KafkaCollectionUpdatesDeserializer::class.java
        return DefaultKafkaConsumerFactory(props)
    }
}

@Service
class KafkaCollectionUpdatesConsumer(private val deserializer: KafkaCollectionUpdatesDeserializer) {
    @KafkaListener(topics = ["COLLECTION_UPDATE"], groupId = "CollectionService")
    fun receiveMessage(consumerRecord: ConsumerRecord<String, String>): KafkaCollectionUpdateDto {
        val message = deserializer.deserialize("COLLECTION_UPDATE", consumerRecord.value().toString().toByteArray())

        return message
    }
}