package org.itmo.fileservice

import org.itmo.fileservice.collection.Collection
import org.itmo.fileservice.utils.CollectionExecutorUtil
import org.itmo.fileservice.utils.KafkaSystemUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.core.scheduler.Schedulers
import kotlin.collections.isNotEmpty

@SpringBootApplication
class FileServiceApplication(
    private val kafkaSystemUtil: KafkaSystemUtil,
    private val collectionExecutor: CollectionExecutorUtil,
    @Autowired private val collection: Collection
) : CommandLineRunner {
    private val baseFilename = "database.json"

    override fun run(vararg args: String?) {
        collectionExecutor
            .getCollectionFromFile(baseFilename, if (args.isNotEmpty()) args[0]?.trim() else null)
            .publishOn(Schedulers.boundedElastic())
            .doOnSuccess {
                collection.getFlats()
                    .map { flats ->
                        flats.forEach { println(it) }
                    }
                    .subscribe()
                kafkaSystemUtil.listenRSSEvent().subscribe()
                kafkaSystemUtil.noticeServiceStarted().subscribe()
            }
            .subscribe()
    }
}

fun main(args: Array<String>) {
    runApplication<FileServiceApplication>(*args)
}
