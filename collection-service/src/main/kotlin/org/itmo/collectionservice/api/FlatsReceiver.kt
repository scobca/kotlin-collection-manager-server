package org.itmo.collectionservice.api

import org.itmo.collectionservice.api.dto.BasicSuccessfulResponse
import org.itmo.collectionservice.api.dto.collection.GetFlatDto
import org.itmo.collectionservice.collection.Collection
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
class FlatsReceiver(
    @Qualifier("fileService")
    private val fileServiceWebClient: WebClient,
    private val collection: Collection
) {
    suspend fun getCollection() {
        val response = fileServiceWebClient
            .get()
            .uri("/service/v1/flats/getAll")
            .retrieve()
            .awaitBody<BasicSuccessfulResponse<List<GetFlatDto>>>()

        response.message.forEach { flat ->
            collection.getFlats()[flat.id] = flat
        }
    }
}