package org.itmo.invokerservice.services

import org.itmo.invokerservice.storages.CommandsStorage
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
class InvokerService(
    commandsStorage: CommandsStorage,
    @Qualifier("collectionService") private val webClient: WebClient,
    private val authService: AuthService,
) {
    private val commandsList = commandsStorage.getAllCommands()
    private val authCommands = listOf<String>("login", "register", "refresh")

    suspend fun handleCommand(message: String): Any? {
        val line = message.trim().split(" ")
        val command = line[0].trim()
        val args = message.split(" ").drop(1)

        if (authCommands.contains(command)) {
            when (command) {
                "login" -> {return authService.login(args)}
                "register" -> {return authService.register(args)}
                "refresh" -> {return authService.refreshTokens(args[0])}
            }
        }

        if (command == "help") {
            return commandsList
        }

        if (commandsList?.contains(command) == true || command == "getElementById") {
            try {
                return if (args.size == 1) {
                    webClient
                        .post()
                        .uri("/collection/${command}")
                        .bodyValue(args.last())
                        .header("Authorization", "Bearer ${args[0]}")
                        .retrieve()
                        .awaitBody<String>()
                } else {
                    println(args[0] + " " + args[1])
                    webClient
                        .post()
                        .uri("/collection/${command}")
                        .bodyValue(args[0])
                        .header("Authorization", "Bearer ${args[1]}")
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