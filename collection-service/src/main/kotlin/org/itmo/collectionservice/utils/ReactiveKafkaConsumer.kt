package org.itmo.collectionservice.utils

import org.itmo.collectionservice.config.dto.KafkaRequireSystemSynchronizationMessage
import org.itmo.collectionservice.config.dto.KafkaSystemMessage
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.kafka.receiver.KafkaReceiver

@Component
class ReactiveKafkaConsumer(private val kafkaReceiver: KafkaReceiver<String, Any>) {
    fun listenAnyEvent(topic: String): Flux<Any> {
        return kafkaReceiver.receive()
            .filter { it.topic() == topic }
            .map { it.value() }
    }

    fun listenSystemEvent(): Flux<KafkaSystemMessage> {
        return kafkaReceiver.receive()
            .filter { it.topic() == "system" }
            .map {it.value() as KafkaSystemMessage}
    }

    fun listenRSSEvent(): Flux<KafkaRequireSystemSynchronizationMessage> {
        return kafkaReceiver.receive()
            .filter { it.topic() == "require-system-synchronization" }
            .map { it.value() as KafkaRequireSystemSynchronizationMessage }
    }
}