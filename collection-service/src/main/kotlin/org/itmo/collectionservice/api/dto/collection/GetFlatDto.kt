package org.itmo.collectionservice.api.dto.collection

import kotlinx.serialization.Serializable
import org.itmo.collectionservice.api.dto.user.UserDto
import org.itmo.collectionservice.collection.items.Furnish
import org.itmo.collectionservice.parser.dto.FlatDto

@Serializable
data class GetFlatDto(
    val id: Long,
    val name: String,
    val coordinates: CoordinatesDto,
    val area: Long,
    val numberOfRooms: Long,
    val price: Long,
    val balcony: Boolean,
    val furnish: Furnish,
    val house: HouseDto,
    val user: UserDto?,
) : Comparable<GetFlatDto> {
    override fun compareTo(other: GetFlatDto): Int {
        val price = this.price
        val otherPrice = other.price

        val area = this.area
        val otherArea = other.area

        return (price.compareTo(otherPrice) + area.compareTo(otherArea))
    }
}

fun GetFlatDto.toSerializable(): FlatDto {
    return FlatDto(
        id = this.id,
        name = this.name,
        coordinates = org.itmo.collectionservice.parser.dto.CoordinatesDto(this.coordinates.x, this.coordinates.y),
        area = this.area,
        numberOfRooms = this.numberOfRooms,
        price = this.price,
        balcony = this.balcony,
        furnish = this.furnish,
        house = org.itmo.collectionservice.parser.dto.HouseDto(
            this.house.name,
            this.house.year,
            this.house.numberOfFloors
        ),
    )
}

fun GetFlatDto.toCreateFlatDto(): CreateFlatDto {
    return CreateFlatDto(
        name = this.name,
        x = this.coordinates.x,
        y = this.coordinates.y,
        area = this.area,
        numberOfRooms = this.numberOfRooms,
        price = this.price,
        balcony = this.balcony,
        furnish = this.furnish,
        houseName = this.house.name,
        year = this.house.year,
        numberOfFloors = this.numberOfRooms,
    )
}

fun GetFlatDto.toUpdateFlatDto(): UpdateFlatDto {
    return UpdateFlatDto(
        id = this.id,
        name = this.name,
        x = this.coordinates.x,
        y = this.coordinates.y,
        area = this.area,
        numberOfRooms = this.numberOfRooms,
        price = this.price,
        balcony = this.balcony,
        furnish = this.furnish,
        houseName = this.house.name,
        year = this.house.year,
        numberOfFloors = this.numberOfRooms,
    )
}