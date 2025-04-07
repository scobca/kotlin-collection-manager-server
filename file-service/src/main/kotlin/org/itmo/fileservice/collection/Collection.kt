package org.itmo.fileservice.collection

import org.itmo.fileservice.collection.items.Flat
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.util.TreeMap

@Component
class Collection {
    private val initDate: ZonedDateTime = ZonedDateTime.now()
    private var flats = TreeMap<Long, Flat>()

    fun getInitDate(): ZonedDateTime = initDate
    fun getFlats(): TreeMap<Long, Flat> = flats

    operator fun get(id: Long): Flat? {
        return flats[id]
    }

    operator fun set(id: Long, value: Flat) {
        flats[id] = value
    }
}