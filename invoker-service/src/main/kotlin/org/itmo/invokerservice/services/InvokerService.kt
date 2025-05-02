package org.itmo.invokerservice.services

import org.itmo.invokerservice.services.dto.WebResponse
import org.itmo.invokerservice.storages.CommandsHistory
import org.itmo.invokerservice.storages.CommandsStorage
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
class InvokerService(
    private val commandsHistory: CommandsHistory,
    commandsStorage: CommandsStorage,
    private val webClient: WebClient,
) {
    private val commandsList = commandsStorage.getAllCommands()

    suspend fun handleCommand(line: String): Any? {
        val split = line.trim().split(" ")
        val command = split[0].trim()
        val args = line.split(" ").drop(1)

        if (command == "help") {
            commandsHistory.addCommand(command)
            return commandsList
        }
        if (command == "history") {
            val response = commandsHistory.getCommandsHistory()
            commandsHistory.addCommand(command)

            return response
        }

        if (commandsList?.contains(command) == true) {
            commandsHistory.addCommand(command)
            val response = webClient
                .post()
                .uri("/collection/${command}")
                .retrieve()
                .awaitBody<Any>() as WebResponse

            return response.message.toString()
        } else {
            return "Command $command not found"
        }
    }
}