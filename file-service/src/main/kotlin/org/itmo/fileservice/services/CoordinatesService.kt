package org.itmo.fileservice.services

import jakarta.transaction.Transactional
import org.itmo.fileservice.dto.collection.coordinates.CreateCoordinatesDto
import org.itmo.fileservice.dto.collection.coordinates.UpdateCoordinatesDto
import org.itmo.fileservice.entities.Coordinates
import org.itmo.fileservice.exceptions.NotFoundException
import org.itmo.fileservice.io.BasicSuccessfulResponse
import org.itmo.fileservice.mappers.CoordinatesMapper
import org.itmo.fileservice.repositories.CoordinatesRepository
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class CoordinatesService(
    private val coordinatesRepository: CoordinatesRepository,
    private val coordinatesMapper: CoordinatesMapper,
) {
    fun getAll() = BasicSuccessfulResponse(coordinatesRepository.findAll())

    fun getCoordinatesById(id: Long, throwable: Boolean = true): BasicSuccessfulResponse<Coordinates> {
        val res = coordinatesRepository.findById(id)
        if ((res == Optional.empty<Coordinates>()) && throwable)
            throw NotFoundException("Coordinates with id: $id not found")

        val coordinates = res.get()
        return coordinates.toHttpResponse()
    }

    @Transactional
    fun createCoordinates(coordinatesDto: CreateCoordinatesDto): BasicSuccessfulResponse<Coordinates> {
        val coordinates = coordinatesRepository.save(coordinatesMapper.coordinatesFromDto(coordinatesDto))

        return coordinates.toHttpResponse()
    }

    @Transactional
    fun updateCoordinates(data: UpdateCoordinatesDto): BasicSuccessfulResponse<String> {
        val coordinates = getCoordinatesById(data.id).message

        data.x?.let { coordinates::x.set(it) }
        data.y?.let { coordinates::y.set(it) }

        return BasicSuccessfulResponse("Coordinates with id: ${data.id} updated successfully.")
    }

    @Transactional
    fun deleteCoordinates(id: Long): BasicSuccessfulResponse<String> {
        getCoordinatesById(id)
        coordinatesRepository.deleteById(id)

        return BasicSuccessfulResponse("Coordinates with id $id deletes successfully.")
    }
}