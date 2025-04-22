package org.itmo.invokerservice.utils

import org.itmo.invokerservice.storages.CommandsHistory
import org.itmo.invokerservice.storages.CommandsStorage
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

data class CommandHttpResponse<T>(
    val status: Int,
    val message: T,
)

@Service
class ApiResolver(
    private val collectionServiceWebClient: WebClient,
    private val commandsStorage: CommandsStorage,
    private val commandsHistory: CommandsHistory
) {
    suspend fun <T> sendCommand(uri: String, body: Any?): Any {

        if (uri == "history") return CommandHttpResponse(HttpStatus.OK.value(), commandsHistory.getCommandsHistory())
        if (uri == "help") return CommandHttpResponse(HttpStatus.OK.value(), commandsStorage.getAllCommands())

        if (commandsStorage.getAllCommands()?.get(uri) == null)
            return CommandHttpResponse(HttpStatus.NOT_FOUND.value(), "Endpoint not found")

        commandsHistory.addCommand(uri)

        return if (body != null) {
            collectionServiceWebClient
                .post()
                .uri(uri)
                .bodyValue(body)
                .retrieve()
                .awaitBody<CommandHttpResponse<T>>()
        } else {
            collectionServiceWebClient
                .post()
                .uri(uri)
                .retrieve()
                .awaitBody<CommandHttpResponse<T>>()
        }
    }
}