package org.itmo.collectionservice.api

import org.itmo.collectionservice.api.dto.BasicSuccessfulResponse
import org.itmo.collectionservice.api.dto.collection.GetFlatDto
import org.itmo.collectionservice.collection.items.Flat
import org.itmo.collectionservice.services.CommandService
import org.itmo.collectionservice.storages.TokensStorage
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.util.TreeMap

@Service
class FlatsReceiver(
    @Qualifier("fileService")
    private val fileServiceWebClient: WebClient,
    private val commandService: CommandService
) {
    suspend fun getCollection() {
        val response = fileServiceWebClient
            .get()
            .uri("/service/v1/flats/getAllByUserId")
            .header("Authorization", "Bearer ${TokensStorage.getAccessToken()}")
            .retrieve()
            .awaitBody<BasicSuccessfulResponse<List<GetFlatDto>>>()

        response.message.forEach { flat ->
            commandService.insert(flat)
        }
    }
}