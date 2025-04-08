package org.itmo.collectionservice.services

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
class CollectionService(private val fileServiceWebClient: WebClient) {
    suspend fun getCollection() {
        val response = fileServiceWebClient
            .get()
            .uri("/collection/getCollection")
            .retrieve()
            .awaitBody<String>()

        println(response)
    }
}