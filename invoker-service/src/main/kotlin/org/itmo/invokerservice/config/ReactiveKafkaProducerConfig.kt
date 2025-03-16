package org.itmo.invokerservice.config

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.support.serializer.JsonSerializer
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderOptions

@Configuration
class ReactiveKafkaProducerConfig {
    @Bean
    fun jsonSenderOptions(): SenderOptions<String, Any> {
        val props = mutableMapOf<String, Any>(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092,localhost:9093,localhost:9094",
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java,
            ProducerConfig.ACKS_CONFIG to "all",
        )
        return SenderOptions.create(props)
    }

    @Bean
    fun jsonKafkaSender(jsonSenderOptions: SenderOptions<String, Any>): KafkaSender<String, Any> {
        return KafkaSender.create(jsonSenderOptions)
    }
}