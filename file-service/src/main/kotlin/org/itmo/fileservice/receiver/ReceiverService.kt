package org.itmo.fileservice.receiver

import org.itmo.fileservice.collection.Collection
import org.itmo.fileservice.collection.items.Flat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.TreeMap

@Service
class ReceiverService(@Autowired private val collection: Collection) {
    fun getFlats(): Mono<TreeMap<Long, Flat>> {
        return collection.getFlats()
    }

    fun insert(flat: Flat): Mono<String?> {
        return flat.getId()
            .flatMap { id ->
                collection.getFlats().flatMap { flats ->
                    flats[id] = flat
                    Mono.just("Flat created successfully")
                }
            }
    }
}