package org.itmo.collectionservice.services

import kotlinx.serialization.Serializable
import org.itmo.collectionservice.annotations.ChangingCollection
import org.itmo.collectionservice.api.dto.BasicSuccessfulResponse
import org.itmo.collectionservice.api.dto.collection.GetFlatDto
import org.itmo.collectionservice.api.dto.collection.toCreateFlatDto
import org.itmo.collectionservice.api.dto.collection.toSerializable
import org.itmo.collectionservice.api.dto.collection.toUpdateFlatDto
import org.itmo.collectionservice.collection.Collection
import org.itmo.collectionservice.controllers.dto.ReplaceIfLowerDto
import org.itmo.collectionservice.parser.dto.FlatDto
import org.itmo.collectionservice.services.dto.CollectionInfoDto
import org.itmo.collectionservice.strategies.StartupStrategy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

data class CommandHttpResponse<T>(
    val status: Int,
    val message: T,
)

@Serializable
data class FlatsResponse(
    val flats: List<FlatDto>
)

@Service
class CommandService(
    @Autowired private val collection: Collection,
    @Qualifier("fileService")
    private val fileServiceWebClient: WebClient,
    private val startup: StartupStrategy
) {
    fun getElementById(id: String): Any {
        val flat = collection[id.toLong()]
        return if (flat != null) {
            return "[${flat.id},${flat.name},${
                flat.coordinates.x
            },${
                flat.coordinates.y
            },${flat.area},${flat.numberOfRooms},${flat.price},${flat.balcony},${flat.furnish},${
                flat.house.name
            },${flat.house.year},${flat.house.numberOfFloors}]"
        } else {
            CommandHttpResponse(HttpStatus.NOT_FOUND.value(), "Flat with id $id not found")
        }

    }

    fun info(): CollectionInfoDto {
        val flatsDto = collection.getFlats().values.map { it.toSerializable() }
        return CollectionInfoDto(
            collection.getFlats()::class.simpleName.toString(),
            collection.getInitDate().toString(),
            flatsDto,
            flatsDto.size
        )
    }

    suspend fun show(): CommandHttpResponse<out Any?> {
        return try {
            val response = fileServiceWebClient
                .get()
                .uri("/service/v1/flats/getAll")
                .retrieve()
                .awaitBody<BasicSuccessfulResponse<List<GetFlatDto>>>()

            if (response.status == HttpStatus.OK.value()) {
                CommandHttpResponse(HttpStatus.OK.value(), response.message)
            } else {
                CommandHttpResponse<String>(response.status, response.message.toString())
            }
        } catch (e: Exception) {
            CommandHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.message)
        }
    }

    @ChangingCollection
    suspend fun insert(flat: GetFlatDto, token: String): CommandHttpResponse<out String?> {
        try {
            val response = fileServiceWebClient
                .post()
                .uri("/service/v1/flats/create")
                .bodyValue(flat.toCreateFlatDto())
                .header("Authorization", "Bearer $token")
                .retrieve()
                .awaitBody<BasicSuccessfulResponse<GetFlatDto>>()

            if (response.status == HttpStatus.OK.value()) {
                collection.getFlats()[flat.id] = flat
                return CommandHttpResponse(HttpStatus.OK.value(), "Flat created")
            } else {
                return CommandHttpResponse(response.status, response.message.toString())
            }
        } catch (e: Exception) {
            return CommandHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.message)
        }
    }

    @ChangingCollection
    suspend fun update(flat: GetFlatDto, token: String): CommandHttpResponse<out String?> {
        val flatId = flat.id
        val oldFlat = collection.getFlats()[flatId]

        if (oldFlat != null) {
            try {
                val response = fileServiceWebClient
                    .patch()
                    .uri("/service/v1/flats/update")
                    .bodyValue(flat.toUpdateFlatDto())
                    .header("Authorization", "Bearer $token")
                    .retrieve()
                    .awaitBody<BasicSuccessfulResponse<String>>()

                if (response.status == HttpStatus.OK.value()) {
                    collection.getFlats()[flat.id] = flat
                    return CommandHttpResponse(HttpStatus.OK.value(), "Flat updated")
                } else {
                    return CommandHttpResponse(response.status, response.message.toString())
                }
            } catch (e: Exception) {
                return CommandHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.message)
            }


        } else {
            return CommandHttpResponse(HttpStatus.NOT_FOUND.value(), "Flat with id $flatId not found.")
        }
    }

    @ChangingCollection
    suspend fun remove(flatId: String, token: String): CommandHttpResponse<out String?> {
        val flat = collection.getFlats()[flatId.toLong()]

        if (flat != null) {
            try {
                val response = fileServiceWebClient
                    .delete()
                    .uri("/service/v1/flats/delete/$flatId")
                    .header("Authorization", "Bearer $token")
                    .retrieve()
                    .awaitBody<BasicSuccessfulResponse<String>>()

                if (response.status == HttpStatus.OK.value()) {
                    collection.getFlats().remove(flatId.toLong())
                    return CommandHttpResponse(HttpStatus.OK.value(), "Flat removed")
                } else {
                    return CommandHttpResponse(response.status, response.message.toString())
                }
            } catch (e: Exception) {
                return CommandHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.message)
            }
        } else {
            return CommandHttpResponse(HttpStatus.NOT_FOUND.value(), "Flat not found")
        }
    }

    @ChangingCollection
    suspend fun removeIfLowerKey(id: String, token: String): CommandHttpResponse<String> {
        val targetId = id.toLong()
        collection.getFlats()
            .keys
            .filter { it < targetId }
            .forEach { remove(it.toString(), token) }

        return CommandHttpResponse(HttpStatus.OK.value(), "Flats removed by lower ID")
    }

    @ChangingCollection
    suspend fun removeAllByBalcony(bool: String, token: String): CommandHttpResponse<String> {
        collection.getFlats()
            .filter { it.value.balcony.toString() == bool }
            .keys
            .forEach { remove(it.toString(), token) }

        return CommandHttpResponse(HttpStatus.OK.value(), "Flats removed by balcony")
    }

    @ChangingCollection
    suspend fun clear(token: String): CommandHttpResponse<out String?> {
        try {
            val response = fileServiceWebClient
                .delete()
                .uri("/service/v1/flats/deleteAll")
                .header("Authorization", "Bearer $token")
                .retrieve()
                .awaitBody<BasicSuccessfulResponse<String>>()

            if (response.status == HttpStatus.OK.value()) {
                collection.getFlats().clear()
                startup.applicationStart()
                return CommandHttpResponse(HttpStatus.OK.value(), "Collection cleaned")
            } else {
                return CommandHttpResponse(response.status, response.message.toString())
            }
        } catch (e: Exception) {
            return CommandHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.message)
        }
    }

    @ChangingCollection
    suspend fun replaceIfLower(body: ReplaceIfLowerDto, token: String): CommandHttpResponse<String> {
        val comparableFlat = collection.getFlats()[body.id]
        return comparableFlat?.let {
            if (it < body.flatDto) {
                collection[body.id] = body.flatDto
                update(body.flatDto, token)
                CommandHttpResponse(HttpStatus.OK.value(), "Flat replaced successfully")
            } else {
                CommandHttpResponse(HttpStatus.BAD_REQUEST.value(), "Flat replaced failed")
            }
        } ?: CommandHttpResponse(HttpStatus.NOT_FOUND.value(), "Comparable flat not found")
    }

    fun getAveragePrice(): CommandHttpResponse<String> {
        val flats = collection.getFlats().values
        val prices = flats.map { it.price }

        return if (prices.isNotEmpty()) {
            val average = prices.average()
            CommandHttpResponse(HttpStatus.OK.value(), "Average price for flats: ${average.toLong()}")
        } else {
            CommandHttpResponse(HttpStatus.NOT_FOUND.value(), "No flats available. Cannot get the average price")
        }
    }

    fun filterContainsName(data: String): CommandHttpResponse<FlatsResponse> {
        val flats = collection.getFlats()

        val response = flats.values
            .filter { it.name.contains(data.trim(), ignoreCase = true) == true }
            .map { it.toSerializable() }

        return CommandHttpResponse(HttpStatus.OK.value(), FlatsResponse(response))
    }
}