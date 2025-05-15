package org.itmo.fileservice.io.handlers

import org.itmo.fileservice.exceptions.DoubleRecordException
import org.itmo.fileservice.exceptions.NotFoundException
import org.itmo.fileservice.io.BasicErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception, request: WebRequest): ResponseEntity<BasicErrorResponse> {
        val errorResponse = BasicErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = ex.message ?: "Internal server error"
        )

        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException, request: WebRequest): ResponseEntity<BasicErrorResponse> {
        val errorResponse = BasicErrorResponse(
            status = ex.status,
            message = ex.message
        )

        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(DoubleRecordException::class)
    fun handleDoubleRecordException(ex: DoubleRecordException, request: WebRequest): ResponseEntity<BasicErrorResponse> {
        val errorResponse = BasicErrorResponse(
            status = ex.status,
            message = ex.message
        )

        return ResponseEntity(errorResponse, HttpStatus.CONFLICT)
    }
}