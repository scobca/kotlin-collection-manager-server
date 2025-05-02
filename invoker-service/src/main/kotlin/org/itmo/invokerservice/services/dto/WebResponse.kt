package org.itmo.invokerservice.services.dto

import org.springframework.http.HttpStatus

data class WebResponse(val code: HttpStatus, val message: String)
