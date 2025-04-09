package org.itmo.collectionservice.parser

import org.itmo.collectionservice.collection.Collection
import org.itmo.collectionservice.collection.items.Coordinates
import org.itmo.collectionservice.collection.items.Flat
import org.itmo.collectionservice.collection.items.Furnish
import org.itmo.collectionservice.collection.items.House
import org.itmo.collectionservice.parser.dto.CoordinatesDto
import org.itmo.collectionservice.parser.dto.FlatDto
import org.itmo.collectionservice.parser.dto.HouseDto
import org.springframework.stereotype.Service

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
class FlatParser(private val collection: Collection) {
    fun prepareCollectionForSending(): List<FlatDto> {
        val flats = collection.getFlats()
        return flats.values.map { flat -> flat.toSerializable() }
    }
}