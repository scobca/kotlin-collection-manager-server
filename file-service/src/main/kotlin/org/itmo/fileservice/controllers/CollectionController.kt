package org.itmo.fileservice.controllers

import org.itmo.fileservice.collection.items.Flat
import org.itmo.fileservice.services.ReceiverService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.TreeMap

@RestController
@RequestMapping("/collection")
class CollectionController(private val receiver: ReceiverService) {
    @GetMapping("/getCollection")
    fun getCollection(): TreeMap<Long, Flat> {
        return receiver.getFlats()
    }
}