package org.itmo.fileservice.config.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.itmo.fileservice.kafka.dto.KafkaCollectionUpdateDto
import org.itmo.fileservice.parser.FlatParser
import org.itmo.fileservice.serializers.KafkaCollectionUpdatesDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.stereotype.Service

@Configuration
class KafkaCollectionUpdatesConsumerConfig {
    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var kafkaConfig: String

    @Bean
    fun kafkaCollectionUpdatesConsumerFactory(): ConsumerFactory<String, KafkaCollectionUpdateDto> {
        val props = HashMap<String, Any>()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaConfig.toString()
        props[ConsumerConfig.GROUP_ID_CONFIG] = "NewTopicGroup"
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = KafkaCollectionUpdatesDeserializer::class.java
        return DefaultKafkaConsumerFactory(props)
    }
}

@Service
class KafkaCollectionUpdatesConsumer(
    private val deserializer: KafkaCollectionUpdatesDeserializer,
    private val parser: FlatParser
) {
    @KafkaListener(topics = ["COLLECTION_UPDATE"], groupId = "FileService")
    fun receiveMessage(consumerRecord: ConsumerRecord<String, String>) {
        val message = deserializer.deserialize("COLLECTION_UPDATE", consumerRecord.value().toString().toByteArray())

        parser.parseFlatsFromKafkaMessage(message.flats)
    }
}