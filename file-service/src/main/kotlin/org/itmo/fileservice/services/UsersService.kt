package org.itmo.fileservice.services

import jakarta.transaction.Transactional
import org.itmo.fileservice.dto.users.CreateUserDto
import org.itmo.fileservice.dto.users.UserTokensDto
import org.itmo.fileservice.entities.Users
import org.itmo.fileservice.exceptions.DoubleRecordException
import org.itmo.fileservice.exceptions.NotFoundException
import org.itmo.fileservice.io.BasicSuccessfulResponse
import org.itmo.fileservice.mappers.UsersMapper
import org.itmo.fileservice.repositories.UsersRepository
import org.itmo.fileservice.utils.JwtUtil
import org.itmo.fileservice.utils.PasswordClassifier
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class UsersService(
    private val usersRepository: UsersRepository,
    private val usersMapper: UsersMapper,
    private val passwordClassifier: PasswordClassifier,
    private val jwtUtil: JwtUtil,
) {
    fun getAllUsers(): BasicSuccessfulResponse<List<Users?>> = BasicSuccessfulResponse(usersRepository.findAll())

    fun getUserById(id: Long): BasicSuccessfulResponse<Users> {
        val res = usersRepository.findById(id)
        if (res == Optional.empty<Users>())
            throw NotFoundException("User with id: $id not found")

        val user = res.get()
        return user.toHttpResponse()
    }

    fun getUserByEmail(email: String): BasicSuccessfulResponse<Users> {
        val res = usersRepository.findByEmail(email)
        if (res == null)
            throw NotFoundException("User with email: $email not found")

        return res.toHttpResponse()
    }

    @Transactional
    fun createUser(userDto: CreateUserDto): BasicSuccessfulResponse<UserTokensDto> {
        val oldUser = usersRepository.findByEmail(userDto.email)

        if (oldUser != null)
            throw DoubleRecordException("User with email ${userDto.email} already exists")

        if (userDto.password != null) {
            userDto.password = passwordClassifier.hashPassword(userDto.password!!)
        }

        val user = usersRepository.save(usersMapper.userFromDto(userDto))
        val res = UserTokensDto(jwtUtil.generateAccessToken(user), jwtUtil.generateRefreshToken(user))

        return BasicSuccessfulResponse<UserTokensDto>(res)
    }
}