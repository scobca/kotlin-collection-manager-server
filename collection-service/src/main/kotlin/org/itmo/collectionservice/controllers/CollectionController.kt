package org.itmo.collectionservice.controllers

import org.itmo.collectionservice.annotations.CommandDescription
import org.itmo.collectionservice.annotations.CommandEndpoint
import org.itmo.collectionservice.collection.items.Flat
import org.itmo.collectionservice.controllers.dto.ReplaceIfLowerDto
import org.itmo.collectionservice.services.CommandHttpResponse
import org.itmo.collectionservice.services.CommandService
import org.itmo.collectionservice.services.dto.CollectionInfoDto
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
    fun getElementById(@RequestBody id: Long): CommandHttpResponse<*> {
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
    fun show(): CommandHttpResponse<TreeMap<Long, Flat>> {
        return commandService.show()
    }

    @CommandEndpoint
    @CommandDescription("Adds a new element with the specified key")
    @PostMapping("/insert")
    fun insert(@RequestBody flat: Flat): CommandHttpResponse<String> {
        return commandService.insert(flat)
    }

    @CommandEndpoint
    @CommandDescription("Updates the command by it's Id")
    @PostMapping("/update")
    fun update(@RequestBody flat: Flat): CommandHttpResponse<String> {
        return commandService.update(flat)
    }

    @CommandEndpoint
    @CommandDescription("Removes an element by it Id")
    @PostMapping("/remove")
    fun remove(@RequestBody flatId: Long): CommandHttpResponse<String> {
        return commandService.remove(flatId)
    }

    @CommandEndpoint
    @CommandDescription("Remove all flats with id lower than arguments id")
    @PostMapping("/removeIfLowerKey")
    fun removeIfLowerKey(@RequestBody id: Long): CommandHttpResponse<String> {
        return commandService.removeIfLowerKey(id)
    }

    @CommandEndpoint
    @CommandDescription("Removes all flats by balcony parameter")
    @PostMapping("/removeAllByBalcony")
    fun removeAllByBalcony(@RequestBody balcony: String): CommandHttpResponse<String> {
        return commandService.removeAllByBalcony(balcony)
    }

    @CommandEndpoint
    @CommandDescription("Clears the flats collection")
    @PostMapping("/clear")
    fun clear(): CommandHttpResponse<String> {
        return commandService.clear()
    }

    @CommandEndpoint
    @CommandDescription("Replace a flat from a collection if it lower than new flat")
    @PostMapping("replaceIfLower")
    fun replaceIfLower(@RequestBody body: ReplaceIfLowerDto): CommandHttpResponse<String> {
        return commandService.replaceIfLower(body)
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
    fun filterContainsName(@RequestBody name: String): CommandHttpResponse<MutableList<Flat>> {
        return commandService.filterContainsName(name)
    }
}