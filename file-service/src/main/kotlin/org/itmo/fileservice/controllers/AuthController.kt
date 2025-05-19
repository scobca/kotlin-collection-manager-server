package org.itmo.fileservice.controllers

import org.itmo.fileservice.dto.auth.AuthUserDto
import org.itmo.fileservice.dto.users.UserTokensDto
import org.itmo.fileservice.io.BasicSuccessfulResponse
import org.itmo.fileservice.services.AuthService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("service/v1/auth")
class AuthController(private val authService: AuthService) {
    @PostMapping("/login")
    fun login(@RequestBody() userData: AuthUserDto): BasicSuccessfulResponse<UserTokensDto> =
        authService.login(userData)

    @PostMapping("/register")
    fun register(@RequestBody() userData: AuthUserDto): BasicSuccessfulResponse<UserTokensDto> =
        authService.register(userData)

    @GetMapping("/updateJwtTokens")
    fun getJwtTokens(@RequestBody() token: String): BasicSuccessfulResponse<UserTokensDto> {
        return authService.getJwtTokens(token)
    }
}