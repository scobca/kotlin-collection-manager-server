package org.itmo.collectionservice.utils

import org.itmo.collectionservice.api.dto.collection.CoordinatesDto
import org.itmo.collectionservice.api.dto.collection.GetFlatDto
import org.itmo.collectionservice.api.dto.collection.HouseDto
import org.itmo.collectionservice.collection.items.Furnish

object StringToFlatParser {
    fun parseToFlat(input: String): GetFlatDto {

        val data = input.replace("[", "").replace("]", "").split(",")

        var flat = GetFlatDto(
            id = data[0].toLong(),
            name = data[1],
            coordinates = CoordinatesDto(x = data[2].toLong(), y = data[3].toFloat()),
            area = data[4].toLong(),
            numberOfRooms = data[5].toLong(),
            price = data[6].toLong(),
            balcony = data[7].toBoolean(),
            furnish = Furnish.valueOf(data[8].uppercase().trim()),
            house = HouseDto(name = data[9], year = data[10].toInt(), numberOfFloors = data[11].toLong()),
            user = null,
        )
        return flat
    }

    fun extractJwt(input: String): String {
        val data = input.replace("[", "").replace("]", "").split(",")
        if (data.size == 13) return data.last()

        return ""
    }

}