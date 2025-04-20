package org.itmo.invokerservice.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.itmo.invokerservice.kafka.dto.KafkaCommandsSynchronizationDto
import org.itmo.invokerservice.serializers.KafkaCommandsSynchronizationDeserializer
import org.itmo.invokerservice.storages.CommandsStorage
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.stereotype.Service

@Configuration
class KafkaCommandsConsumerConfig {
    @Bean
    fun kafkaCommandsSynchronizationConsumerFactory(): ConsumerFactory<String, KafkaCommandsSynchronizationDto> {
        val props = HashMap<String, Any>()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9091"
        props[ConsumerConfig.GROUP_ID_CONFIG] = "InvokerService"
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = KafkaCommandsSynchronizationDeserializer::class.java

        return DefaultKafkaConsumerFactory(props)
    }
}

@Service
class KafkaCommandsSynchronizationConsumer(
    private val deserializer: KafkaCommandsSynchronizationDeserializer,
    private val commandsStorage: CommandsStorage
) {
    @KafkaListener(topics = ["COMMANDS_SYNCHRONIZATION"], groupId = "InvokerService")
    fun receiveMessage(consumerRecord: ConsumerRecord<String, String>) {
        val message =
            deserializer.deserialize("COMMANDS_SYNCHRONIZATION", consumerRecord.value().toString().toByteArray())

        println("Received message: $message")
        commandsStorage.setCommands(message.commands)
    }
}