package org.itmo.invokerservice.config.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.itmo.invokerservice.kafka.dto.KafkaSystemMessageDto
import org.itmo.invokerservice.serializers.KafkaSystemMessageDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.stereotype.Service

@Configuration
class KafkaConsumerConfig {
    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var kafkaServerConfig: String

    @Bean
    fun kafkaSystemMessageConsumerFactory(): ConsumerFactory<String, KafkaSystemMessageDto> {
        val props = HashMap<String, Any>()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaServerConfig
        props[ConsumerConfig.GROUP_ID_CONFIG] = "InvokerService"
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = KafkaSystemMessageDeserializer::class.java
        return DefaultKafkaConsumerFactory(props)
    }
}

@Service
class KafkaSystemMessagesConsumer(private val deserializer: KafkaSystemMessageDeserializer) {
    @KafkaListener(topics = ["SYSTEM"], groupId = "InvokerService")
    fun receiveMessage(consumerRecord: ConsumerRecord<String, String>) {
        val message = deserializer.deserialize("SYSTEM", consumerRecord.value().toString().toByteArray())
    }
}