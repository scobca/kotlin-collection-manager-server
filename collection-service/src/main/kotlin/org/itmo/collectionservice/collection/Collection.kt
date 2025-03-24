package org.itmo.collectionservice.collection

import org.itmo.collectionservice.collection.items.Flat
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.ZonedDateTime
import java.util.*

/**
 * A component for storing and managing a collection of Flat objects.
 * Provides functionality for adding, receiving, and deleting objects by their IDs.
 */
@Component
class Collection {
    /**
     * Date and time of collection initialization.
     */
    private val initDate: ZonedDateTime = ZonedDateTime.now()

    /**
     * A sorted map of Flat objects, where the key is the flat identifier.
     */
    private var flats = TreeMap<Long, Flat>()

    /**
     * Returns the date and time of collection initialization.
     *
     * @return Initialization date and time.
     */
    fun getInitDate(): Mono<ZonedDateTime> = Mono.just(initDate)

    /**
     * Returns the entire collection of Flat objects.
     *
     * @return is a collection of Flat objects.
     */
    fun getFlats(): Mono<TreeMap<Long, Flat>> = Mono.just(flats)

    /**
     * Returns the Flat object by its ID.
     * If there is no object with this ID, returns null.
     *
     * @param id is the flat ID.
     * @return A Flat or null object.
     */
    operator fun get(id: Long): Mono<Flat> {
        return Mono.justOrEmpty(flats[id])
    }

    /**
     * Adds or updates a Flat object to the collection by its ID.
     *
     * @param id is the flat ID.
     * @param value Flat object to add or update.
     */
    operator fun set(id: Long, value: Flat) {
        flats[id] = value
    }
}