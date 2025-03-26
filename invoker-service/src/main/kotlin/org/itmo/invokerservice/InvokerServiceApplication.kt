package org.itmo.invokerservice

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InvokerServiceApplication() : CommandLineRunner {
    override fun run(vararg args: String?) {}
}

fun main(args: Array<String>) {
    runApplication<InvokerServiceApplication>(*args)
}
