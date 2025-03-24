package org.itmo.fileservice

import org.itmo.fileservice.config.dto.KafkaSystemMessage
import org.itmo.fileservice.config.enums.SystemServices
import org.itmo.fileservice.config.enums.SystemThemes
import org.itmo.fileservice.utils.ReactiveKafkaConsumer
import org.itmo.fileservice.utils.ReactiveKafkaProducer
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.core.scheduler.Schedulers

@SpringBootApplication
class FileServiceApplication(
    private val producer: ReactiveKafkaProducer,
    private val consumer: ReactiveKafkaConsumer
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        val message = KafkaSystemMessage(SystemThemes.SERVICE_STARTED, SystemServices.FILE_SERVICE, null)
        producer.sendEvent("system", message)
            .doFinally { println("Message sent") }
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe()

        consumer.listenRSSEvent()
            .doOnNext { it -> println(it.message) }
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe(
                { },
                { error -> println("Error: $error") }
            )
    }
}

fun main(args: Array<String>) {
    runApplication<FileServiceApplication>(*args)
}
