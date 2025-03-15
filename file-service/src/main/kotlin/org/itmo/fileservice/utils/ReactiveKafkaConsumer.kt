package org.itmo.fileservice.utils

import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.kafka.receiver.KafkaReceiver

@Component
class ReactiveKafkaConsumer(private val kafkaReceiver: KafkaReceiver<String, Any>) {
    fun processEvents(topic: String): Flux<Any> {
        return kafkaReceiver.receive()
            .filter { it.topic() == topic }
            .map { it.value() }
    }
}