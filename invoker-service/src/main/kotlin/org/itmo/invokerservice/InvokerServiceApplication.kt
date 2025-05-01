package org.itmo.invokerservice

import org.itmo.invokerservice.config.kafka.KafkaSystemProducer
import org.itmo.invokerservice.kafka.dto.KafkaSystemMessageDto
import org.itmo.invokerservice.kafka.enums.KafkaServices
import org.itmo.invokerservice.kafka.enums.KafkaSystemThemes
import org.itmo.invokerservice.utils.TcpServerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InvokerServiceApplication(private val kafkaSystemProducer: KafkaSystemProducer) : CommandLineRunner {
    override fun run(vararg args: String?) {
        kafkaSystemProducer.sendEvent(
            KafkaSystemMessageDto(
                KafkaSystemThemes.SERVICE_STARTED,
                KafkaServices.INVOKER_SERVICE,
                null
            )
        )

        TcpServerFactory.startTcpServer()
    }
}

fun main(args: Array<String>) {
    runApplication<InvokerServiceApplication>(*args)
}
