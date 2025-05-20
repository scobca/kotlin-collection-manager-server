package org.itmo.fileservice.services

import jakarta.transaction.Transactional
import org.itmo.fileservice.dto.collection.house.CreateHouseDto
import org.itmo.fileservice.dto.collection.house.UpdateHouseDto
import org.itmo.fileservice.entities.Houses
import org.itmo.fileservice.exceptions.NotFoundException
import org.itmo.fileservice.io.BasicSuccessfulResponse
import org.itmo.fileservice.mappers.HouseMapper
import org.itmo.fileservice.repositories.HousesRepository
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class HousesService(
    private val housesRepository: HousesRepository,
    private val houseMapper: HouseMapper
) {
    fun getAll() = BasicSuccessfulResponse(housesRepository.findAll())

    fun getHouseById(id: Long, throwable: Boolean = true): BasicSuccessfulResponse<Houses> {
        val res = housesRepository.findById(id)
        if ((res == Optional.empty<Houses>()) && throwable)
            throw NotFoundException("Coordinates with id: $id not found")

        val house = res.get()
        return house.toHttpResponse()
    }

    @Transactional
    fun createHouse(houseDto: CreateHouseDto): BasicSuccessfulResponse<Houses> {
        val house = housesRepository.save(houseMapper.houseFromDto(houseDto))

        return BasicSuccessfulResponse(house)
    }

    @Transactional
    fun updateHouse(data: UpdateHouseDto): BasicSuccessfulResponse<String> {
        val house = getHouseById(data.id).message

        data.name?.let { house::name.set(it) }
        data.year?.let { house::year.set(it) }
        data.numberOfFloors?.let { house::numberOfFloors.set(it) }

        return BasicSuccessfulResponse("House with id: ${data.id} updated successfully.")
    }

    @Transactional
    fun deleteById(id: Long): BasicSuccessfulResponse<String> {
        getHouseById(id)
        housesRepository.deleteById(id)

        return BasicSuccessfulResponse("House with id: $id deleted successfully.")
    }
}