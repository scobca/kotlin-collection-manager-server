package org.itmo.collectionservice

import org.itmo.collectionservice.services.CommandEndpointService
import org.itmo.collectionservice.strategies.StartupStrategy
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CollectionServiceApplication(
    private val startup: StartupStrategy,
    private val apiEndpointService: CommandEndpointService
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val commands = apiEndpointService.getApiEndpoint()
        startup.applicationStart()
    }
}

fun main(args: Array<String>) {
    runApplication<CollectionServiceApplication>(*args)
}
