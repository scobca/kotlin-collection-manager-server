package org.itmo.invokerservice.api.dto

import org.springframework.http.HttpStatus

data class BasicSuccessfulResponse<T>(
    override val message: T
) : AbstractSuccessfulResponse<T>(HttpStatus.OK.value(), message)