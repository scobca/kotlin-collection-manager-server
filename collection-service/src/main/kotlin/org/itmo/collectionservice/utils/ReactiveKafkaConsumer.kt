package org.itmo.collectionservice.utils

import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.kafka.receiver.KafkaReceiver

@Component
class ReactiveKafkaConsumer(private val kafkaReceiver: KafkaReceiver<String, Any>) {
    fun listenEvent(topic: String): Flux<Any> {
        return kafkaReceiver.receive()
            .filter { it.topic() == topic }
            .map { it.value() }
    }
}