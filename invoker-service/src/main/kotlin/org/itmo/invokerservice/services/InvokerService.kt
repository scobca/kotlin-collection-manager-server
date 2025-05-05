package org.itmo.invokerservice.services

import org.itmo.invokerservice.services.dto.WebResponse
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

        if (commandsList?.contains(command) == true) {
            val response = webClient
                .post()
                .uri("/collection/${command}")
                .bodyValue(args)
                .retrieve()
                .awaitBody<Any>() as WebResponse

            return response.message.toString()
        } else {
            return "Command $command not found"
        }
    }
}