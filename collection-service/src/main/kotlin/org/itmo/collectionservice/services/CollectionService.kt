package org.itmo.collectionservice.services

import org.itmo.collectionservice.collection.items.Flat
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.util.TreeMap

@Service
class CollectionService(
    private val fileServiceWebClient: WebClient,
    private val commandService: CommandService
) {
    suspend fun getCollection() {
        val response = fileServiceWebClient
            .get()
            .uri("/collection/getCollection")
            .retrieve()
            .awaitBody<TreeMap<Long, Flat>>()

        response.forEach { flat ->
            commandService.insert(flat.value)
        }
    }
}