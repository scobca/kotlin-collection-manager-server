package org.itmo.fileservice.services

import jakarta.transaction.Transactional
import org.itmo.fileservice.dto.collection.coordinates.CreateCoordinatesDto
import org.itmo.fileservice.dto.collection.flat.CreateFlatDto
import org.itmo.fileservice.dto.collection.flat.CreateFlatInternalDto
import org.itmo.fileservice.dto.collection.flat.UpdateFlatDto
import org.itmo.fileservice.dto.collection.house.CreateHouseDto
import org.itmo.fileservice.entities.Flats
import org.itmo.fileservice.entities.Users
import org.itmo.fileservice.exceptions.AccessDeniedException
import org.itmo.fileservice.exceptions.NotFoundException
import org.itmo.fileservice.io.BasicSuccessfulResponse
import org.itmo.fileservice.mappers.FlatMapper
import org.itmo.fileservice.repositories.FlatsRepository
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.Optional

@Service
class FlatsService(
    private val flatsRepository: FlatsRepository,
    private val flatMapper: FlatMapper,
    private val coordinatesService: CoordinatesService,
    private val housesService: HousesService,
) {
    fun getAll() = BasicSuccessfulResponse(flatsRepository.findAll())

    fun getByUserId(user: Users) = BasicSuccessfulResponse(flatsRepository.findByUserId(user.id))

    fun getFlatById(id: Long, user: Users, throwable: Boolean = true): BasicSuccessfulResponse<Flats> {
        val res = flatsRepository.findById(id)
        if ((res == Optional.empty<Flats>()) && throwable)
            throw NotFoundException("Flat with id: $id not found")

        if ((res.get().user.id != user.id)) throw AccessDeniedException("This collection item does not belong to you.")

        val flat = res.get()
        return flat.toHttpResponse()
    }

    @Transactional
    fun createFlat(flatDto: CreateFlatDto, user: Users): BasicSuccessfulResponse<Flats> {
        val coordinates = coordinatesService.createCoordinates(CreateCoordinatesDto(flatDto.x, flatDto.y))
        val house = housesService.createHouse(CreateHouseDto(flatDto.houseName, flatDto.year, flatDto.numberOfFloors))

        println(coordinates)
        println(house)

        val internalFlatDto = CreateFlatInternalDto(
            id = flatDto.id,
            name = flatDto.name,
            coordinates = coordinates.message,
            area = flatDto.area,
            numberOfRooms = flatDto.numberOfRooms,
            price = flatDto.price,
            balcony = flatDto.balcony,
            furnish = flatDto.furnish,
            house = house.message,
            user = user,
            createdAt = ZonedDateTime.now(),
        )

        val flat = flatsRepository.save(flatMapper.flatFromDto(internalFlatDto))

        return flat.toHttpResponse()
    }

    @Transactional
    fun updateFlat(data: UpdateFlatDto, user: Users): BasicSuccessfulResponse<String> {
        val flat = getFlatById(data.id, user).message
        val coordinates = flat.coordinates
        val house = flat.house

        data.name?.let { flat::name.set(it) }
        data.x?.let { coordinates::x.set(it) }
        data.y?.let { coordinates::y.set(it) }
        data.area?.let { flat::area.set(it) }
        data.numberOfRooms?.let { flat::numberOfRooms.set(it) }
        data.price?.let { flat::price.set(it) }
        data.balcony?.let { flat::balcony.set(it) }
        data.furnish?.let { flat::furnish.set(it) }
        data.houseName?.let { house::name.set(it) }
        data.year?.let { house::year.set(it) }
        data.numberOfFloors?.let { house::numberOfFloors.set(it) }

        return BasicSuccessfulResponse("Flat with id: ${flat.id} updated successfully.")
    }

    @Transactional
    fun deleteFlat(id: Long): BasicSuccessfulResponse<String> {
        flatsRepository.deleteById(id)

        return BasicSuccessfulResponse("Flat with id: $id deleted successfully.")
    }

    @Transactional
    fun deleteAll(user: Users): BasicSuccessfulResponse<String> {
        getByUserId(user).message.filter { user.id == it.user.id }.forEach { flat ->
            flatsRepository.deleteById(flat.id)
        }

        return BasicSuccessfulResponse("All flats deleted.")
    }
}