package org.itmo.invokerservice.api.dto

abstract class AbstractSuccessfulResponse<T> (
    val status: Int,
    open val message: T?
)