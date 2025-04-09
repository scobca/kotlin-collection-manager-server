package org.itmo.collectionservice.services.dto

import org.itmo.collectionservice.collection.items.Flat
import java.time.ZonedDateTime
import java.util.TreeMap

data class CollectionInfoDto(
    private val type: String,
    private val initDate: ZonedDateTime,
    private val flats: TreeMap<Long, Flat>,
    private val collectionSize: Int,
)
