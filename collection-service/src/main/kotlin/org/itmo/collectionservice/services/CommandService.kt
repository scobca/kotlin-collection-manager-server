package org.itmo.collectionservice.services

import org.itmo.collectionservice.collection.Collection
import org.itmo.collectionservice.collection.items.Flat
import org.itmo.collectionservice.controllers.dto.ReplaceIfLowerDto
import org.itmo.collectionservice.services.dto.CollectionInfoDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.TreeMap

data class CommandHttpResponse<T>(
    val status: HttpStatus,
    val message: T,
)

@Service
class CommandService(@Autowired private val collection: Collection) {
    fun getElementById(id: Long): CommandHttpResponse<*> {
        val flat = collection[id]
        return if (flat != null) {
            CommandHttpResponse(HttpStatus.OK, flat)
        } else {
            CommandHttpResponse(HttpStatus.NOT_FOUND, null)
        }

    }

    fun info(): CollectionInfoDto {
        return CollectionInfoDto(
            collection.getFlats()::class.simpleName.toString(),
            collection.getInitDate(),
            collection.getFlats(),
            collection.getFlats().size
        )
    }

    fun show(): CommandHttpResponse<TreeMap<Long, Flat>> {
        val flats = collection.getFlats()
        return CommandHttpResponse<TreeMap<Long, Flat>>(HttpStatus.OK, flats)
    }

    fun insert(flat: Flat): CommandHttpResponse<String> {
        collection.getFlats()[flat.getId()] = flat
        return CommandHttpResponse(HttpStatus.OK, "Flat created")
    }

    fun update(flat: Flat): CommandHttpResponse<String> {
        val flatId = flat.getId()
        val oldFlat = collection.getFlats()[flatId]

        if (oldFlat != null) {
            collection.getFlats()[flat.getId()] = flat
            return CommandHttpResponse(HttpStatus.OK, "Flat updated")
        } else {
            return CommandHttpResponse(HttpStatus.NOT_FOUND, "Flat not found")
        }
    }

    fun remove(flatId: Long): CommandHttpResponse<String> {
        val flat = collection.getFlats()[flatId]

        if (flat != null) {
            collection.getFlats().remove(flatId)
            return CommandHttpResponse(HttpStatus.OK, "Flat removed")
        } else {
            return CommandHttpResponse(HttpStatus.NOT_FOUND, "Flat not found")
        }
    }

    fun removeIfLowerKey(id: Long): CommandHttpResponse<String> {
        val flats = collection.getFlats()
        val removableFlats = mutableListOf<Long>()

        flats.forEach { flat ->
            if (flat.key < id) {
                removableFlats.add(flat.key)
            }
        }

        removableFlats.forEach { flat -> remove(flat) }

        return CommandHttpResponse(HttpStatus.OK, "Flats removed by lower ID")
    }

    fun removeAllByBalcony(bool: String): CommandHttpResponse<String> {
        val flats = collection.getFlats()
        val removableFlats = mutableListOf<Long>()

        flats.forEach { flat ->
            if (flat.value.getBalcony().toString() == bool) {
                removableFlats.add(flat.key)
            }
        }

        removableFlats.forEach { flat -> remove(flat) }

        return CommandHttpResponse(HttpStatus.OK, "Flats removed by balcony")
    }

    fun clear(): CommandHttpResponse<String> {
        collection.getFlats().clear()

        return CommandHttpResponse(HttpStatus.OK, "Collection cleaned")
    }

    fun replaceIfLower(body: ReplaceIfLowerDto): CommandHttpResponse<String> {
        val comparableFlat = collection.getFlats()[body.id]

        if (comparableFlat != null && comparableFlat < body.flat) {
            collection[body.id] = body.flat
            return CommandHttpResponse(HttpStatus.OK, "Flat replaced successfully")
        } else return CommandHttpResponse(HttpStatus.BAD_REQUEST, "Flat replaced failed")
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
            return CommandHttpResponse(HttpStatus.OK, "Average price for flats: ${allPrices / flatsCount}")
        } else {
            return CommandHttpResponse(HttpStatus.NOT_FOUND, "No flats available. Cannot get the average price")
        }
    }

    fun filterContainsName(name: String): CommandHttpResponse<MutableList<Flat>> {
        val flats = collection.getFlats()
        val response = mutableListOf<Flat>()

        flats.forEach { flat ->
            if (flat.value.getName() != null) {
                if (flat.value.getName()!!.trim().contains(name.trim(), ignoreCase = true)) response.add(flat.value)
            }
        }
        return CommandHttpResponse(HttpStatus.OK, response)
    }
}