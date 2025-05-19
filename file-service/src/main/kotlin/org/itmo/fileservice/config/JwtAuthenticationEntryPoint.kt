package org.itmo.fileservice.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.itmo.fileservice.exceptions.JwtAuthenticationException
import org.itmo.fileservice.utils.JwtUtil
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationEntryPoint(private val jwtUtil: JwtUtil) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        response?.contentType = "application/json"
        val auth = request?.getHeader("Authorization")

        if (auth != null && auth.startsWith("Bearer ")) {
            val jwtToken = auth.substring(7)

            try {
                jwtUtil.verifyToken(jwtToken)
            } catch (_: JwtAuthenticationException) {
                response?.status = HttpServletResponse.SC_FORBIDDEN
                val body = """{"status": ${HttpStatus.FORBIDDEN.value()}, "message": "Token expired"}"""

                response?.writer?.write(body)
            }
        } else {
            response?.status = HttpServletResponse.SC_UNAUTHORIZED
            val body =
                """{"status": ${HttpStatus.UNAUTHORIZED.value()}, "message": "You'll be authorized to access this resource"}"""

            response?.writer?.write(body)
        }
    }
}