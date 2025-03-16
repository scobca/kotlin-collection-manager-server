package org.itmo.collectionservice.utils

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderRecord

@Component
class ReactiveKafkaProducer(private val kafkaSender: KafkaSender<String, Any>) {
    fun sendEvent(topic: String, event: Any): Mono<Void> {
        val record = SenderRecord.create<String, Any, Any>(
            topic,
            null,
            System.currentTimeMillis(),
            null,
            event,
            null
        )

        return kafkaSender.send(Mono.just(record))
            .then()
            .doOnError {ex -> println(ex.message) }
    }
}