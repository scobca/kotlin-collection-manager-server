package org.itmo.fileservice.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.Jwts.SIG
import io.jsonwebtoken.security.Keys
import org.itmo.fileservice.entities.Users
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.Date

@Component
class JwtUtil() {
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
        val claims = getClaims(token)

        return claims?.expiration?.after(Date()) == true
    }

    private fun getClaims(token: String): Claims? {
        return Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(jwtSecret.toByteArray()))
            .build()
            .parseSignedClaims(token)
            .payload
    }
}
