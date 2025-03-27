package org.itmo.invokerservice.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.itmo.invokerservice.kafka.dto.KafkaCollectionUpdateDto
import org.itmo.invokerservice.serializers.KafkaCollectionUpdatesDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.stereotype.Service

@Configuration
class KafkaCollectionUpdatesConsumerConfig {
    @Bean
    fun kafkaCollectionUpdatesConsumerFactory(): ConsumerFactory<String, KafkaCollectionUpdateDto> {
        val props = HashMap<String, Any>()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9091"
        props[ConsumerConfig.GROUP_ID_CONFIG] = "NewTopicGroup"
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = KafkaCollectionUpdatesDeserializer::class.java
        return DefaultKafkaConsumerFactory(props)
    }
}

@Service
class KafkaCollectionUpdatesConsumer {
    @KafkaListener(topics = ["COLLECTION_UPDATE"], groupId = "NewTopicGroup")
    fun receiveMessage(consumerRecord: ConsumerRecord<String, KafkaCollectionUpdateDto>) {
        println(consumerRecord.value())
    }
}