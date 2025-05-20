package org.itmo.fileservice.controllers

import org.itmo.fileservice.dto.collection.flat.CreateFlatDto
import org.itmo.fileservice.dto.collection.flat.UpdateFlatDto
import org.itmo.fileservice.entities.Flats
import org.itmo.fileservice.io.BasicSuccessfulResponse
import org.itmo.fileservice.services.FlatsService
import org.itmo.fileservice.utils.JwtUtil
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/service/v1/flats")
class FlatsController(
    private val flatsService: FlatsService,
    private val jwtUtil: JwtUtil,
) {
    @GetMapping("/getAll")
    fun getAll() = flatsService.getAll()

    @GetMapping("/getAllByUserId")
    fun getAllByUserId(@RequestHeader("Authorization") authHeader: String): BasicSuccessfulResponse<List<Flats>> {
        val jwtToken = authHeader.removePrefix("Bearer ")
        val user = jwtUtil.getUserFromToken(jwtToken)

        return flatsService.getByUserId(user)
    }

    @PostMapping("/create")
    fun createFlat(
        @RequestBody() data: CreateFlatDto,
        @RequestHeader("Authorization") authHeader: String
    ): BasicSuccessfulResponse<Flats> {
        val jwtToken = authHeader.removePrefix("Bearer ")
        val user = jwtUtil.getUserFromToken(jwtToken)

        return flatsService.createFlat(data, user)
    }

    @PatchMapping("/update")
    fun updateFlat(
        @RequestBody() data: UpdateFlatDto,
        @RequestHeader("Authorization") authHeader: String,
    ): BasicSuccessfulResponse<String> {
        val jwtToken = authHeader.removePrefix("Bearer ")
        val user = jwtUtil.getUserFromToken(jwtToken)

        return flatsService.updateFlat(data, user)
    }

    @DeleteMapping("/delete/{id}")
    fun deleteFlat(
        @PathVariable("id") id: Long,
        @RequestHeader("Authorization") authHeader: String
    ): BasicSuccessfulResponse<String> {
        val jwtToken = authHeader.removePrefix("Bearer ")
        val user = jwtUtil.getUserFromToken(jwtToken)

        return flatsService.deleteFlat(id, user)
    }
}