package org.itmo.fileservice.dto.users

import org.itmo.fileservice.entities.Convertable
import org.itmo.fileservice.io.BasicSuccessfulResponse

data class UserTokensDto(
    val accessToken: String,
    val refreshToken: String,
) : Convertable<UserTokensDto> {
    override fun toHttpResponse(): BasicSuccessfulResponse<UserTokensDto> = BasicSuccessfulResponse(this)

}
