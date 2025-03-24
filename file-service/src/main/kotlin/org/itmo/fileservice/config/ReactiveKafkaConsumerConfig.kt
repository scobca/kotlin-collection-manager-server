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
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
            ConsumerConfig.GROUP_ID_CONFIG to "file-service-group",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to CustomJsonDeserializer::class.java,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
            ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG to "1000",
            ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG to "60000",
        )
        props["spring.json.trusted.packages"] = "*"

        return ReceiverOptions.create<String, Any>(props)
            .subscription(
                listOf(
                    "system",
                    "require-system-synchronization",
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
}