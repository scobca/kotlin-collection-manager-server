package org.itmo.fileservice.io

abstract class AbstractSuccessfulResponse<T> (
    val status: Int,
    open val message: T?
)