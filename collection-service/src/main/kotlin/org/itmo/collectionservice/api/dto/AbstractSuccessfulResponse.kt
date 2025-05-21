package org.itmo.collectionservice.api.dto

abstract class AbstractSuccessfulResponse<T> (
    val status: Int,
    open val message: T?
)