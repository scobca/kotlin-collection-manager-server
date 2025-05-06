package org.itmo.collectionservice.utils

import org.itmo.collectionservice.collection.items.Coordinates
import org.itmo.collectionservice.collection.items.Flat
import org.itmo.collectionservice.collection.items.Furnish
import org.itmo.collectionservice.collection.items.House

object StringToFlatParser {
    fun parseToFlat(input: String): Flat {

        val data = input.replace("[", "").replace("]", "").split(",")

        var flat = Flat()
            .setId(data[0].toLong())
            .setName(data[1])
            .setCoordinates(Coordinates(data[2].toLong(), data[3].toFloat()))
            .setArea(data[4].toLong())
            .setNumberOfRooms(data[5].toLong())
            .setPrice(data[6].toLong())
            .setBalcony(data[7].toBoolean())
            .setFurnish(Furnish.valueOf(data[8].uppercase().trim()))
            .setHouse(House(data[9], data[10].toInt(), data[11].toLong()))

        return flat
    }

}