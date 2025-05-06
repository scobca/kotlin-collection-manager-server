package org.itmo.invokerservice.services

import org.itmo.invokerservice.storages.CommandsStorage
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
class InvokerService(
    commandsStorage: CommandsStorage,
    private val webClient: WebClient,
) {
    private val commandsList = commandsStorage.getAllCommands()

    suspend fun handleCommand(message: String): Any? {
        val line = message.trim().split(" ")
        val command = line[0].trim()
        val args = message.split(" ").drop(1)

        if (command == "help") {
            return commandsList
        }

        println(args)

        if (commandsList?.contains(command) == true || command == "getElementById") {
            try {
                return if (!args.isEmpty()) {
                    webClient
                        .post()
                        .uri("/collection/${command}")
                        .bodyValue(args.last())
                        .retrieve()
                        .awaitBody<String>()
                } else {
                    webClient
                        .post()
                        .uri("/collection/${command}")
                        .bodyValue(args)
                        .retrieve()
                        .awaitBody<Any>()
                }

            } catch (e: Exception) {
                return e.message
            }
        } else {
            return "Command $command not found"
        }
    }
}