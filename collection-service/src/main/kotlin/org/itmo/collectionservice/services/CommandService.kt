package org.itmo.collectionservice.services

import kotlinx.serialization.Serializable
import org.itmo.collectionservice.annotations.ChangingCollection
import org.itmo.collectionservice.collection.Collection
import org.itmo.collectionservice.collection.items.Flat
import org.itmo.collectionservice.controllers.dto.ReplaceIfLowerDto
import org.itmo.collectionservice.parser.dto.FlatDto
import org.itmo.collectionservice.parser.toSerializable
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
            return "[${flat.getId()},${flat.getHouse()?.getName()},${
                flat.getCoordinates()?.getX()
            },${
                flat.getCoordinates()?.getY()
            },${flat.getArea()},${flat.getNumberOfRooms()},${flat.getPrice()},${flat.getBalcony()},${flat.getFurnish()},${
                flat.getHouse()?.getName()
            },${flat.getHouse()?.getYear()},${flat.getHouse()?.getNumberOfFloors()}]"
        } else {
            CommandHttpResponse(HttpStatus.NOT_FOUND.value(), "Flat with id $id not found")
        }

    }

    fun info(): CollectionInfoDto {
        var flatsDto = mutableListOf<FlatDto>()
        val flats = collection.getFlats()

        flats.forEach { flat -> flatsDto.add(flat.value.toSerializable()) }

        return CollectionInfoDto(
            collection.getFlats()::class.simpleName.toString(),
            collection.getInitDate().toString(),
            flatsDto,
            collection.getFlats().size
        )
    }

    fun show(): CommandHttpResponse<TreeMap<Long, Flat>> {
        val flats = collection.getFlats()
        return CommandHttpResponse<TreeMap<Long, Flat>>(HttpStatus.OK.value(), flats)
    }

    @ChangingCollection
    fun insert(flat: Flat): CommandHttpResponse<String> {
        collection.getFlats()[flat.getId()] = flat
        return CommandHttpResponse(HttpStatus.OK.value(), "Flat created")
    }

    @ChangingCollection
    fun update(flat: Flat): CommandHttpResponse<String> {
        val flatId = flat.getId()
        val oldFlat = collection.getFlats()[flatId]

        if (oldFlat != null) {
            collection.getFlats()[flat.getId()] = flat
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
        val flats = collection.getFlats()
        val removableFlats = mutableListOf<Long>()

        flats.forEach { flat ->
            if (flat.key < id.toLong()) {
                removableFlats.add(flat.key)
            }
        }

        removableFlats.forEach { flat -> remove(flat.toString()) }

        return CommandHttpResponse(HttpStatus.OK.value(), "Flats removed by lower ID")
    }

    @ChangingCollection
    fun removeAllByBalcony(bool: String): CommandHttpResponse<String> {
        val flats = collection.getFlats()
        val removableFlats = mutableListOf<Long>()

        flats.forEach { flat ->
            if (flat.value.getBalcony().toString() == bool) {
                removableFlats.add(flat.key)
            }
        }

        removableFlats.forEach { flat -> remove(flat.toString()) }

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
        println(comparableFlat)

        if (comparableFlat == null) {
            return CommandHttpResponse(HttpStatus.NOT_FOUND.value(), "Comparable flat not found")
        } else {
            if (comparableFlat < body.flatDto) {
                collection[body.id] = body.flatDto
                return CommandHttpResponse(HttpStatus.OK.value(), "Flat replaced successfully")
            } else return CommandHttpResponse(HttpStatus.BAD_REQUEST.value(), "Flat replaced failed")
        }
    }

    fun getAveragePrice(): CommandHttpResponse<String> {
        var allPrices: Long = 0
        val flatsCount = collection.getFlats().size

        if (flatsCount > 0) {
            collection.getFlats().forEach { flat ->
                if (flat.value.getPrice() != null) {
                    allPrices += flat.value.getPrice()!!
                }
            }
            return CommandHttpResponse(HttpStatus.OK.value(), "Average price for flats: ${allPrices / flatsCount}")
        } else {
            return CommandHttpResponse(HttpStatus.NOT_FOUND.value(), "No flats available. Cannot get the average price")
        }
    }

    fun filterContainsName(data: String): CommandHttpResponse<FlatsResponse> {
        val flats = collection.getFlats()

        val response = mutableListOf<FlatDto>()

        flats.forEach { flat ->
            if (flat.value.getName() != null) {
                if (flat.value.getName()!!.trim().contains(data.trim(), ignoreCase = true)) {
                    response.add(flat.value.toSerializable())
                }
            }
        }

        return CommandHttpResponse(HttpStatus.OK.value(), FlatsResponse(response.toList()))
    }
}