package org.itmo.invokerservice.services

import org.itmo.invokerservice.api.dto.BasicSuccessfulResponse
import org.itmo.invokerservice.services.dto.WebResponse
import org.itmo.invokerservice.services.dto.auth.AuthUserDto
import org.itmo.invokerservice.services.dto.auth.UserTokensDto
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
class AuthService(
    @Qualifier("fileService")
    private val fileServiceWebClient: WebClient,
) {
    suspend fun login(data: List<String>): Any {

        try {
            val login = data[0]
            val password = data[1]

            val response = fileServiceWebClient
                .post()
                .uri("/service/v1/auth/login")
                .bodyValue(AuthUserDto(login, password))
                .retrieve()
                .awaitBody<BasicSuccessfulResponse<UserTokensDto>>()

            return if (response.status == HttpStatus.OK.value()) {
                response.message
            } else {
                WebResponse(HttpStatus.INTERNAL_SERVER_ERROR, response.message.toString())
            }
        } catch (e: Exception) {
            return WebResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.message.toString())
        }
    }

    suspend fun register(data: List<String>): Any {

        try {
            val login = data[0]
            val password = data[1]

            val response = fileServiceWebClient
                .post()
                .uri("/service/v1/auth/register")
                .bodyValue(AuthUserDto(login, password))
                .retrieve()
                .awaitBody<BasicSuccessfulResponse<UserTokensDto>>()

            return if (response.status == HttpStatus.OK.value()) {
                response.message
            } else {
                WebResponse(HttpStatus.INTERNAL_SERVER_ERROR, response.message.toString())
            }
        } catch (e: Exception) {
            return WebResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.message.toString())
        }
    }

    suspend fun refreshTokens(refreshToken: String): Any {
        try {
            val response = fileServiceWebClient
                .post()
                .uri("/service/v1/auth/updateTokens/updateJwtTokens")
                .bodyValue(refreshToken)
                .retrieve()
                .awaitBody<BasicSuccessfulResponse<UserTokensDto>>()

            return if (response.status == HttpStatus.OK.value()) {
                response.message
            } else {
                WebResponse(HttpStatus.INTERNAL_SERVER_ERROR, response.message.toString())
            }
        } catch (e: Exception) {
            return WebResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.message.toString())
        }
    }
}