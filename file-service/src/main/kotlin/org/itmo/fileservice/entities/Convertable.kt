package org.itmo.fileservice.entities

import org.itmo.fileservice.io.BasicSuccessfulResponse

interface Convertable<T> {
    fun toHttpResponse(): BasicSuccessfulResponse<T>
}