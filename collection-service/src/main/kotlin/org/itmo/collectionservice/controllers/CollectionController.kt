package org.itmo.collectionservice.controllers

import org.itmo.collectionservice.annotations.CommandDescription
import org.itmo.collectionservice.annotations.CommandEndpoint
import org.itmo.collectionservice.api.dto.collection.GetFlatDto
import org.itmo.collectionservice.controllers.dto.ReplaceIfLowerDto
import org.itmo.collectionservice.services.CommandHttpResponse
import org.itmo.collectionservice.services.CommandService
import org.itmo.collectionservice.services.FlatsResponse
import org.itmo.collectionservice.services.dto.CollectionInfoDto
import org.itmo.collectionservice.utils.StringToFlatParser
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.TreeMap

@RestController
@RequestMapping("/collection")
class CollectionController(private val commandService: CommandService) {

    @CommandEndpoint
    @CommandDescription("Return collection element by it ID")
    @PostMapping("/getElementById")
    fun getElementById(@RequestBody id: String): Any {
        return commandService.getElementById(id)
    }

    @CommandEndpoint
    @CommandDescription("Display information about the collection")
    @PostMapping("/info")
    fun info(): CollectionInfoDto {
        return commandService.info()
    }

    @CommandEndpoint
    @CommandDescription("Outputs all the elements of the collection in a string representation")
    @PostMapping("/show")
    fun show(): CommandHttpResponse<TreeMap<Long, GetFlatDto>> {
        return commandService.show()
    }

    @CommandEndpoint
    @CommandDescription("Adds a new element with the specified key")
    @PostMapping("/insert")
    suspend fun insert(@RequestBody flatString: String): CommandHttpResponse<out String?> {
        try {
            val token = StringToFlatParser.extractJwt(flatString)
            val flat = StringToFlatParser.parseToFlat(flatString)

            val oldFlat = getElementById(flat.id.toString())

            if (oldFlat.toString().contains("[")) return CommandHttpResponse(
                HttpStatus.CONFLICT.value(),
                "Flat with this ID already exists"
            )

            return commandService.insert(flat, token)
        } catch (e: Exception) {
            return CommandHttpResponse(HttpStatus.BAD_REQUEST.value(), e.message.toString())
        }
    }

    @CommandEndpoint
    @CommandDescription("Updates the command by it's Id")
    @PostMapping("/update")
    suspend fun update(@RequestBody flatString: String): CommandHttpResponse<out String?> {
        try {
            val token = StringToFlatParser.extractJwt(flatString)
            val flat = StringToFlatParser.parseToFlat(flatString)

            return commandService.update(flat, token)
        } catch (e: Exception) {
            return CommandHttpResponse(HttpStatus.BAD_REQUEST.value(), e.message.toString())
        }
    }

    @CommandEndpoint
    @CommandDescription("Removes an element by it Id")
    @PostMapping("/remove")
    suspend fun remove(@RequestBody flatId: String): CommandHttpResponse<out String?> {
        val token = flatId.split(" ")[1]
        return commandService.remove(flatId.split(" ")[0], token)
    }

    @CommandEndpoint
    @CommandDescription("Remove all flats with id lower than arguments id")
    @PostMapping("/removeIfLowerKey")
    suspend fun removeIfLowerKey(@RequestBody id: String): CommandHttpResponse<String> {
        val token = id.split(" ")[1]
        return commandService.removeIfLowerKey(id.split(" ")[0], token)
    }

    @CommandEndpoint
    @CommandDescription("Removes all flats by balcony parameter")
    @PostMapping("/removeAllByBalcony")
    suspend fun removeAllByBalcony(@RequestBody balcony: String): CommandHttpResponse<String> {
        val token = balcony.split(" ")[1]
        return commandService.removeAllByBalcony(balcony.split(" ")[0], token)
    }

    @CommandEndpoint
    @CommandDescription("Clears the flats collection")
    @PostMapping("/clear")
    suspend fun clear(@RequestBody() token: String): CommandHttpResponse<out String?> {
        println(token)
        return commandService.clear(token.trim())
    }

    @CommandEndpoint
    @CommandDescription("Replace a flat from a collection if it lower than new flat")
    @PostMapping("/replaceIfLower")
    suspend fun replaceIfLower(@RequestBody bodyString: String): CommandHttpResponse<String> {
        try {
            val token = StringToFlatParser.extractJwt(bodyString)
            val flat = StringToFlatParser.parseToFlat(bodyString)
            val body = ReplaceIfLowerDto(flat.id, flat)

            return commandService.replaceIfLower(body, token)
        } catch (e: Exception) {
            return CommandHttpResponse(HttpStatus.CONFLICT.value(), e.message.toString())
        }
    }

    @CommandEndpoint
    @CommandDescription("Return average prices of all flats in collection")
    @PostMapping("/getAveragePrice")
    fun getAveragePrice(): CommandHttpResponse<String> {
        return commandService.getAveragePrice()
    }

    @CommandEndpoint
    @CommandDescription("Return all flats, which name contains similar word from key")
    @PostMapping("/filterContainsName")
    fun filterContainsName(@RequestBody data: String): CommandHttpResponse<FlatsResponse> {
        return commandService.filterContainsName(data)
    }
}