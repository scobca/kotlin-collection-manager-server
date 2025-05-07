package org.itmo.fileservice.services

import org.itmo.fileservice.collection.Collection
import org.itmo.fileservice.collection.items.Flat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.TreeMap

@Component
class ReceiverService(@Autowired private var collection: Collection) {
    fun getFlats(): TreeMap<Long, Flat> {
        return collection.getFlats()
    }

    fun insert(flat: Flat) {
        collection.getFlats()[flat.getId()] = flat
    }

    fun clear() {
        collection.getFlats().clear()
    }
}