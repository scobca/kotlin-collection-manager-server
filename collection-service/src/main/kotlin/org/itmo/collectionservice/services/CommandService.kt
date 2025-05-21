package org.itmo.collectionservice.services

import kotlinx.serialization.Serializable
import org.itmo.collectionservice.annotations.ChangingCollection
import org.itmo.collectionservice.api.dto.collection.GetFlatDto
import org.itmo.collectionservice.api.dto.collection.toSerializable
import org.itmo.collectionservice.collection.Collection
import org.itmo.collectionservice.controllers.dto.ReplaceIfLowerDto
import org.itmo.collectionservice.parser.dto.FlatDto
import org.itmo.collectionservice.services.dto.CollectionInfoDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.TreeMap

data class CommandHttpResponse<T>(
    val status: Int,
    val message: T,
)

@Serializable
data class FlatsResponse(
    val flats: List<FlatDto>
)

@Service
class CommandService(@Autowired private val collection: Collection) {
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

    fun show(): CommandHttpResponse<TreeMap<Long, GetFlatDto>> {
        return CommandHttpResponse<TreeMap<Long, GetFlatDto>>(HttpStatus.OK.value(), collection.getFlats())
    }

    @ChangingCollection
    fun insert(flat: GetFlatDto): CommandHttpResponse<String> {
        collection.getFlats()[flat.id] = flat
        return CommandHttpResponse(HttpStatus.OK.value(), "Flat created")
    }

    @ChangingCollection
    fun update(flat: GetFlatDto): CommandHttpResponse<String> {
        val flatId = flat.id
        val oldFlat = collection.getFlats()[flatId]

        if (oldFlat != null) {
            collection.getFlats()[flat.id] = flat
            return CommandHttpResponse(HttpStatus.OK.value(), "Flat updated")
        } else {
            insert(flat)
            return CommandHttpResponse(HttpStatus.OK.value(), "Flat created")
        }
    }

    @ChangingCollection
    fun remove(flatId: String): CommandHttpResponse<String> {
        val flat = collection.getFlats()[flatId.toLong()]

        if (flat != null) {
            collection.getFlats().remove(flatId.toLong())
            return CommandHttpResponse(HttpStatus.OK.value(), "Flat removed")
        } else {
            return CommandHttpResponse(HttpStatus.NOT_FOUND.value(), "Flat not found")
        }
    }

    @ChangingCollection
    fun removeIfLowerKey(id: String): CommandHttpResponse<String> {
        val targetId = id.toLong()
        collection.getFlats()
            .keys
            .filter { it < targetId }
            .forEach { remove(it.toString()) }

        return CommandHttpResponse(HttpStatus.OK.value(), "Flats removed by lower ID")
    }

    @ChangingCollection
    fun removeAllByBalcony(bool: String): CommandHttpResponse<String> {
        collection.getFlats()
            .filter { it.value.balcony.toString() == bool }
            .keys
            .forEach { remove(it.toString()) }

        return CommandHttpResponse(HttpStatus.OK.value(), "Flats removed by balcony")
    }

    @ChangingCollection
    fun clear(): CommandHttpResponse<String> {
        collection.getFlats().clear()

        return CommandHttpResponse(HttpStatus.OK.value(), "Collection cleaned")
    }

    @ChangingCollection
    fun replaceIfLower(body: ReplaceIfLowerDto): CommandHttpResponse<String> {
        val comparableFlat = collection.getFlats()[body.id]
        return comparableFlat?.let {
            if (it < body.flatDto) {
                collection[body.id] = body.flatDto
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