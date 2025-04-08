package org.itmo.fileservice.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/collection")
class CollectionController {
    @GetMapping("/getCollection")
    fun getCollection(): String {
        return "Hello"
    }
}