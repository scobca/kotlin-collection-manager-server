package org.itmo.fileservice.parser

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import org.itmo.fileservice.collection.items.Coordinates
import org.itmo.fileservice.collection.items.Flat
import org.itmo.fileservice.collection.items.Furnish
import org.itmo.fileservice.collection.items.House
import org.itmo.fileservice.parser.dto.CoordinatesDto
import org.itmo.fileservice.parser.dto.FlatDto
import org.itmo.fileservice.parser.dto.HouseDto
import org.itmo.fileservice.services.ReceiverService
import org.itmo.fileservice.utils.GlobalStorage
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.TreeMap

fun Flat.toSerializable(): FlatDto {
    return FlatDto(
        id = this.getId(),
        name = this.getName(),
        coordinates = CoordinatesDto(this.getCoordinates()?.getX(), this.getCoordinates()?.getY()),
        area = this.getArea(),
        numberOfRooms = this.getNumberOfRooms(),
        price = this.getPrice(),
        balcony = this.getBalcony(),
        furnish = this.getFurnish(),
        house = HouseDto(this.getHouse()?.getName(), this.getHouse()?.getYear(), this.getHouse()?.getNumberOfFloors()),
    )
}

fun FlatDto.toFlat(): Flat {
    val flat = Flat()

    flat.setId(this.id)
    flat.setName(this.name ?: "")
    flat.setCoordinates(Coordinates(this.coordinates?.x, this.coordinates?.y))
    flat.setArea(this.area ?: 0L)
    flat.setNumberOfRooms(this.numberOfRooms ?: 0L)
    flat.setPrice(this.price ?: 0L)
    flat.setBalcony(this.balcony == true)
    flat.setFurnish(this.furnish ?: Furnish.NONE)
    flat.setHouse(House(this.house?.name, this.house?.year, this.house?.numberOfFloors))

    return flat
}

@Service
class FlatParser(private val receiver: ReceiverService) {
    @OptIn(ExperimentalSerializationApi::class)
    fun parseFromJson(inputStream: InputStream) {
        try {
            val flats = Json.decodeFromStream<List<FlatDto>>(inputStream)

            flats.forEach { flatDto -> receiver.insert(flatDto.toFlat()) }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun parseToJson(flats: TreeMap<Long, Flat>) {
        val filename = GlobalStorage.getNewSaveFilename()
        val resource = File(filename)

        try {
            if (resource.exists()) resource.delete()
            resource.createNewFile()

            val flatsJson = flats.values.map { flat -> flat.toSerializable() }

            FileOutputStream(resource).use { writer ->
                Json.encodeToStream(flatsJson, writer)
            }

            println("Квартиры успешно сохранены в файл $filename")
        } catch (e: Exception) {
            println("Unexpected error, please try again. Cause: ${e.message}")
        }
    }
}
