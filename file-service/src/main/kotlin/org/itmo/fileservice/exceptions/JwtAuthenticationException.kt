package org.itmo.fileservice.exceptions

import javax.naming.AuthenticationException

class JwtAuthenticationException(message: String) : AuthenticationException(message)