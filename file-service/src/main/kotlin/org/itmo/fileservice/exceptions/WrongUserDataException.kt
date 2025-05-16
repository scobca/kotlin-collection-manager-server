package org.itmo.fileservice.exceptions

import org.springframework.http.HttpStatus

class WrongUserDataException(override val message: String) :
    AbstractHttpException(HttpStatus.UNAUTHORIZED.value(), message)