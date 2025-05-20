package org.itmo.fileservice.mappers

import org.itmo.fileservice.dto.collection.coordinates.CreateCoordinatesDto
import org.itmo.fileservice.entities.Coordinates
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface CoordinatesMapper {
    fun coordinatesToDto(o: Coordinates): CreateCoordinatesDto
    fun coordinatesFromDto(o: CreateCoordinatesDto): Coordinates
}