package org.itmo.fileservice.mappers

import org.itmo.fileservice.dto.collection.house.CreateHouseDto
import org.itmo.fileservice.entities.Houses
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface HouseMapper {
    fun houseToDto(o: Houses): CreateHouseDto
    fun houseFromDto(o: CreateHouseDto): Houses
}