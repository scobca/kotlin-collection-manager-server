package org.itmo.fileservice.parser

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import org.itmo.fileservice.collection.items.Coordinates
import org.itmo.fileservice.collection.items.Flat
import org.itmo.fileservice.collection.items.Furnish
import org.itmo.fileservice.collection.items.House
import org.itmo.fileservice.receiver.ReceiverService
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.io.InputStream
import java.io.InputStreamReader

@Component
class JsonParser(
    private val receiverService: ReceiverService,
    private val mapper: ObjectMapper
) {
    fun loadFlats(inputStream: InputStream): Flux<Flat> {

        return Flux.using(
            { InputStreamReader(inputStream) },
            { reader: InputStreamReader ->
                try {
                    val flatsJson: Array<FlatJson> = mapper.readValue(reader, Array<FlatJson>::class.java)
                    Flux.fromIterable(flatsJson.asIterable())
                        .map { flatJson ->
                            val newFlat = Flat()

                            flatJson.id?.let { newFlat.setId(it) }
                            flatJson.name?.let { newFlat.setName(it) }
                            flatJson.coordinates?.let { newFlat.setCoordinates(Coordinates(it.x, it.y)) }
                            flatJson.area?.let { newFlat.setArea(it) }
                            flatJson.price?.let { newFlat.setPrice(it) }
                            flatJson.balcony?.let { newFlat.setBalcony(it) }
                            flatJson.furnish?.let { Furnish.valueOf(it) }?.let { newFlat.setFurnish(it) }
                            flatJson.house?.let { House(it.name, it.year, it.numberOfFloors) }
                                ?.let { newFlat.setHouse(it) }

                            newFlat
                        }
                        .publishOn(Schedulers.boundedElastic())
                        .flatMap { flat ->
                            receiverService.insert(flat)
                                .thenReturn(flat)
                        }
                } catch (e: InvalidFormatException) {
                    println("File was damaged. Please, check it and try again")
                    Flux.empty<Flat>()
                } catch (e: Exception) {
                    println("Unexpected error, please try again")
                    Flux.empty<Flat>()
                }
            }
        )
    }

}