package org.itmo.fileservice.utils

import org.itmo.fileservice.services.UsersService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

@Component
class PasswordClassifier(@Lazy private val usersService: UsersService) {
    @Value("\${config.password-salt}")
    private lateinit var preSalt: String

    fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-512")
        val salt = preSalt.toByteArray(charset("UTF-8"))

        md.update(salt)

        val hashedBytes = md.digest(password.toByteArray(StandardCharsets.UTF_8))

        return hashedBytes.joinToString("") { "%02x".format(it) }
    }

    fun verifyPassword(userId: Long, password: String): Boolean {
        val user = usersService.getUserById(userId).message
        val hashedPassword = hashPassword(password)

        return user.password == hashedPassword
    }
}