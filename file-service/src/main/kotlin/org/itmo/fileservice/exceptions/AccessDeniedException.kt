package org.itmo.fileservice.exceptions

import org.springframework.http.HttpStatus

class AccessDeniedException(override val message: String) :
    AbstractHttpException(HttpStatus.FORBIDDEN.value(), message)