package org.itmo.fileservice.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.support.serializer.JsonDeserializer
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions

@Configuration
class ReactiveKafkaConsumerConfig {
    @Bean
    fun receiverOptions(): ReceiverOptions<String, Any> {
        val props = mutableMapOf<String, Any>(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092,localhost:9093",
            ConsumerConfig.GROUP_ID_CONFIG to "my-group",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java
        )
        return ReceiverOptions.create(props)
    }

    @Bean
    fun kafkaReceiver(receiverOptions: ReceiverOptions<String, Any>): KafkaReceiver<String, Any> {
        return KafkaReceiver.create(receiverOptions)
    }
}