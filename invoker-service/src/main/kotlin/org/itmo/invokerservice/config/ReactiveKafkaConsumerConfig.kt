package org.itmo.invokerservice.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions

@Configuration
class ReactiveKafkaConsumerConfig {
    @Bean
    fun receiverOptions(): ReceiverOptions<String, Any> {
        val props = mutableMapOf<String, Any>(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092,localhost:9093,localhost:9094",
            ConsumerConfig.GROUP_ID_CONFIG to "invoker-service-group",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to CustomJsonDeserializer::class.java,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "latest",
            ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG to 30000,
            ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG to 10000
        )
        return ReceiverOptions.create<String, Any>(props)
            .subscription(
                listOf(
                    "system",
                    "system-synchronization",
                    "commands-synchronization",
                    "collection-synchronization",
                    "error",
                    "command-request",
                    "command-response",
                    "collection-changes"
                )
            )
    }

    @Bean
    fun kafkaReceiver(receiverOptions: ReceiverOptions<String, Any>): KafkaReceiver<String, Any> {
        return KafkaReceiver.create(receiverOptions)
    }

    @Bean
    fun customJsonDeserializer(): CustomJsonDeserializer {
        return CustomJsonDeserializer(ObjectMapper(), "org.itmo.invokerservice")
    }
}