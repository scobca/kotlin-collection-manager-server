package org.itmo.fileservice.io

import org.springframework.http.HttpStatus

data class BasicSuccessfulResponse<T>(
    override val message: T?
) : AbstractSuccessfulResponse<T>(HttpStatus.OK.value(), message)