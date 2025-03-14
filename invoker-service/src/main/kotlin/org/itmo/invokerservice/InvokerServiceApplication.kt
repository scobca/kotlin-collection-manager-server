package org.itmo.invokerservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InvokerServiceApplication

fun main(args: Array<String>) {
    runApplication<InvokerServiceApplication>(*args)
}
