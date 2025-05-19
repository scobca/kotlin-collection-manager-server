package org.itmo.fileservice.exceptions

import org.springframework.http.HttpStatus

class InvalidJwtTokenException(override val message: String) :
    AbstractHttpException(HttpStatus.UNAUTHORIZED.value(), message)