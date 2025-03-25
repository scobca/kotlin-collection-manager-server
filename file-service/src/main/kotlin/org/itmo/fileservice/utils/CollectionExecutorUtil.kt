package org.itmo.fileservice.utils

import org.itmo.fileservice.collection.Collection
import org.itmo.fileservice.parser.JsonParser
import org.itmo.fileservice.receiver.ReceiverService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.File
import java.io.FileInputStream

@Component
class CollectionExecutorUtil(
    private val parser: JsonParser,
    private val receiverService: ReceiverService,
    @Autowired private val collection: Collection
) {
    fun getCollectionFromFile(filename: String, args: String?): Mono<Void> {
        val resource = if (args.isNullOrEmpty()) {
            filename.substringBeforeLast(".") + ".json"
        } else {
            args.substringBeforeLast(".") + ".json"
        }

        println("collection copying from $resource")

        return Mono.fromCallable { File(resource) }
            .flatMap { file ->
                if (!file.exists() || file.length() == 0L) {
                    Mono.error(IllegalArgumentException("File not found or empty: $resource"))
                } else {
                    Mono.just(file)
                }
            }
            .flatMap { file ->
                parser.loadFlats(FileInputStream(file))
                    .publishOn(Schedulers.boundedElastic())
                    .flatMap { flat -> receiverService.insert(flat) }
                    .then(receiverService.getFlats())
            }
            .doOnNext { println("Collection loaded") }
            .then()
            .onErrorResume { error ->
                println("Error: ${error.message}")
                Mono.empty()
            }
    }
}