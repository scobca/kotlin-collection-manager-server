package org.itmo.collectionservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CollectionServiceApplication

fun main(args: Array<String>) {
    runApplication<CollectionServiceApplication>(*args)
}
