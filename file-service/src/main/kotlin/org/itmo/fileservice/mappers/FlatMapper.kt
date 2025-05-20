package org.itmo.fileservice.mappers

import org.itmo.fileservice.dto.collection.flat.CreateFlatInternalDto
import org.itmo.fileservice.entities.Flats
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface FlatMapper {
    fun flatToDto(o: Flats): CreateFlatInternalDto
    fun flatFromDto(o: CreateFlatInternalDto): Flats
}