package org.itmo.collectionservice

import org.itmo.collectionservice.strategies.StartupStrategy
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CollectionServiceApplication(private val startup: StartupStrategy) : CommandLineRunner {
    override fun run(vararg args: String?) {
        startup.applicationStart()
    }
}

fun main(args: Array<String>) {
    runApplication<CollectionServiceApplication>(*args)
}
