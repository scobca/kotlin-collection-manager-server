package org.itmo.fileservice.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.Jwts.SIG
import io.jsonwebtoken.security.Keys
import org.itmo.fileservice.entities.Users
import org.itmo.fileservice.exceptions.InvalidJwtTokenException
import org.itmo.fileservice.exceptions.JwtAuthenticationException
import org.itmo.fileservice.services.UsersService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.Date

@Component
class JwtUtil(@Lazy private val usersService: UsersService) {
    @Value("\${config.jwt.secret}")
    private lateinit var jwtSecret: String

    @Value("\${config.jwt.access_token_expiration}")
    private lateinit var accessExpirationTime: String

    @Value("\${config.jwt.refresh_token_expiration}")
    private lateinit var refreshExpirationTime: String

    fun generateAccessToken(user: Users): String = generateToken(user, accessExpirationTime)

    fun generateRefreshToken(user: Users): String = generateToken(user, refreshExpirationTime, true)

    private fun generateToken(user: Users, expiration: String, accessToken: Boolean = false): String {
        val claims: MutableMap<String, Any> = mutableMapOf()
        claims["id"] = user.id
        if (!accessToken) claims["email"] = user.email

        return Jwts.builder()
            .claims(claims)
            .issuedAt(Date())
            .expiration(Date.from(Instant.now().plusSeconds(expiration.toLong())))
            .signWith(Keys.hmacShaKeyFor(jwtSecret.toByteArray()), SIG.HS512)
            .compact()
    }

    fun verifyToken(token: String): Boolean {
        try {
            val claims = getClaims(token) ?: throw JwtAuthenticationException("Invalid token claims")
            if (!claims.expiration.after(Date())) {
                throw JwtAuthenticationException("Token expired")
            }
            return true
        } catch (e: Exception) {
            throw JwtAuthenticationException("Invalid token: ${e.message}")
        }
    }

    fun updateRefreshToken(refreshToken: String): String {
        verifyToken(refreshToken)

        val claims = getClaims(refreshToken)!!
        val expiration = claims.expiration
        val issuedAt = claims.issuedAt

        val totalLifetime = expiration?.time?.minus(issuedAt.time)
        val remainingLifetime = expiration?.time?.minus(Date().time)

        if (remainingLifetime != null) {
            totalLifetime?.times(0.25)?.let {
                if (remainingLifetime < it) {
                    val user = getUserFromToken(refreshToken)

                    return generateRefreshToken(user!!)
                } else {
                    return refreshToken
                }
            }
        }
        throw InvalidJwtTokenException("Invalid refresh token.")
    }

    fun getUserFromToken(token: String): Users? {
        val claims = getClaims(token)
        val user = usersService.getUserById((claims?.get("id") as Int).toLong())

        return user.message
    }

    fun getClaims(token: String): Claims? {
        val claims =  Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(jwtSecret.toByteArray()))
            .build()
            .parseSignedClaims(token)
            .payload

        return claims
    }
}
