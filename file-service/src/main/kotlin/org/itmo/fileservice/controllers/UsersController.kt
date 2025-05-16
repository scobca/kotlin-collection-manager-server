package org.itmo.fileservice.controllers

import org.itmo.fileservice.dto.users.CreateUserDto
import org.itmo.fileservice.entities.Users
import org.itmo.fileservice.io.BasicSuccessfulResponse
import org.itmo.fileservice.services.UsersService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/service/v1/users")
class UsersController(private val usersService: UsersService) {
    @GetMapping("/getAll")
    fun getAll(): BasicSuccessfulResponse<List<Users?>> = usersService.getAllUsers()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): BasicSuccessfulResponse<Users> = usersService.getUserById(id)


    @GetMapping("/getByEmail/{email}")
    fun getByEmail(@PathVariable email: String): BasicSuccessfulResponse<Users> = usersService.getUserByEmail(email)

    @PostMapping("/create")
    fun create(@RequestBody userDto: CreateUserDto) = usersService.createUser(userDto)
}