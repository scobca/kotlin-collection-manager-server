package org.itmo.collectionservice.collection

import org.itmo.collectionservice.api.dto.collection.GetFlatDto
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.util.TreeMap
import kotlin.collections.set

@Component
class Collection {
    private val initDate: ZonedDateTime = ZonedDateTime.now()
    private var flats = TreeMap<Long, GetFlatDto>()

    fun getInitDate(): ZonedDateTime = initDate
    fun getFlats(): TreeMap<Long, GetFlatDto> = flats

    operator fun get(id: Long): GetFlatDto? {
        return flats[id]
    }

    operator fun set(id: Long, value: GetFlatDto) {
        flats[id] = value
    }
}