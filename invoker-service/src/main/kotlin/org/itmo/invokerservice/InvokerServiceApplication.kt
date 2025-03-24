package org.itmo.invokerservice

import org.itmo.invokerservice.config.dto.KafkaRequireSystemSynchronizationMessage
import org.itmo.invokerservice.config.enums.SystemThemes
import org.itmo.invokerservice.utils.ReactiveKafkaConsumer
import org.itmo.invokerservice.utils.ReactiveKafkaProducer
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.core.scheduler.Schedulers

@SpringBootApplication
class InvokerServiceApplication(
    private val producer: ReactiveKafkaProducer,
    private val consumer: ReactiveKafkaConsumer
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        consumer.listenSystemEvent()
            .doOnEach { it ->
                println(it)
                if (it.get()?.theme == SystemThemes.SERVICE_STARTED) {
                    producer.sendEvent(
                        "require-system-synchronization",
                        KafkaRequireSystemSynchronizationMessage("Required")
                    )
                        .subscribeOn(Schedulers.boundedElastic())
                        .subscribe()
                    println("Required")
                }
                println("mapped")

            }
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe(
                { println("Event approved") },
                { error -> println("Error: $error") }
            )
    }
}

fun main(args: Array<String>) {
    runApplication<InvokerServiceApplication>(*args)
}
