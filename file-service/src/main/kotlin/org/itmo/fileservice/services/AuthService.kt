package org.itmo.fileservice.services

import org.itmo.fileservice.dto.auth.AuthUserDto
import org.itmo.fileservice.dto.users.CreateUserDto
import org.itmo.fileservice.dto.users.UserTokensDto
import org.itmo.fileservice.exceptions.DoubleRecordException
import org.itmo.fileservice.exceptions.InvalidJwtTokenException
import org.itmo.fileservice.exceptions.JwtAuthenticationException
import org.itmo.fileservice.exceptions.WrongUserDataException
import org.itmo.fileservice.io.BasicSuccessfulResponse
import org.itmo.fileservice.repositories.UsersRepository
import org.itmo.fileservice.utils.JwtUtil
import org.itmo.fileservice.utils.PasswordClassifier
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val usersService: UsersService,
    private val usersRepository: UsersRepository,
    private val passwordClassifier: PasswordClassifier,
    private val jwtUtil: JwtUtil,
) {
    fun login(userData: AuthUserDto): BasicSuccessfulResponse<UserTokensDto> {
        val user = usersService.getUserByEmail(userData.email).message

        if (userData.password != null && user.password != null) {
            val password = passwordClassifier.hashPassword(userData.password!!)

            if (user.password != password) throw WrongUserDataException("Wrong user's email or password. Try again.")

            return UserTokensDto(jwtUtil.generateAccessToken(user), jwtUtil.generateRefreshToken(user)).toHttpResponse()
        } else if (userData.password == null && user.password == null) {
            return UserTokensDto(jwtUtil.generateAccessToken(user), jwtUtil.generateRefreshToken(user)).toHttpResponse()
        }
        throw WrongUserDataException("Wrong user's email or password. Try again.")
    }

    fun register(userData: AuthUserDto): BasicSuccessfulResponse<UserTokensDto> {
        val oldUser = usersRepository.findByEmail(userData.email)
        if (oldUser != null) throw DoubleRecordException("User with email ${userData.email} already exists.")

        if (userData.password != null) userData.password = passwordClassifier.hashPassword(userData.password!!)

        val dto = CreateUserDto(userData.email, userData.password)

        usersService.createUser(dto)
        val user = usersService.getUserByEmail(userData.email).message

        return UserTokensDto(jwtUtil.generateAccessToken(user), jwtUtil.generateRefreshToken(user)).toHttpResponse()
    }

    fun getJwtTokens(refreshToken: String): BasicSuccessfulResponse<UserTokensDto> {
        try {
            jwtUtil.verifyToken(refreshToken)

            val user = jwtUtil.getUserFromToken(refreshToken)
            val res =
                user?.let { UserTokensDto(jwtUtil.generateAccessToken(it), jwtUtil.updateRefreshToken(refreshToken)) }

            return res?.toHttpResponse() ?: throw InvalidJwtTokenException("Refresh token is invalid. Try again.")
        } catch (_: JwtAuthenticationException) {
            throw InvalidJwtTokenException("Refresh token is invalid. Try again.")
        }
    }
}