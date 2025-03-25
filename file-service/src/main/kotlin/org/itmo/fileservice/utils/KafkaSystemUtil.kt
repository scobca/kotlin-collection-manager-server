package org.itmo.fileservice.utils

import org.itmo.fileservice.config.dto.KafkaSystemMessage
import org.itmo.fileservice.config.enums.SystemServices
import org.itmo.fileservice.config.enums.SystemThemes
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers

@Service
class KafkaSystemUtil(
    private val producer: ReactiveKafkaProducer,
    private val consumer: ReactiveKafkaConsumer
) {
    private val startMessage = KafkaSystemMessage(
        SystemThemes.SERVICE_STARTED,
        SystemServices.FILE_SERVICE,
        null
    )

    fun noticeServiceStarted() = producer.sendEvent("system", startMessage)
        .doOnSuccess { println("Message sent") }
        .subscribeOn(Schedulers.boundedElastic())

    fun listenRSSEvent(): Flux<Void> {
        return consumer.listenRSSEvent()
            .flatMap {
                producer.sendEvent("system-synchronization", "collection")
                    .doFinally { println("Collection sent") }
                    .publishOn(Schedulers.boundedElastic())
            }
            .onErrorResume { Flux.empty() }
            .doOnError { error -> println("Error: $error") }
    }

}