package org.itmo.fileservice.exceptions

import org.springframework.http.HttpStatus

class DoubleRecordException(override val message: String) :
    AbstractHttpException(HttpStatus.CONFLICT.value(), message)