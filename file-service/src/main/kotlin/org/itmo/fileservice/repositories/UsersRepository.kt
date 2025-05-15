package org.itmo.fileservice.repositories

import org.itmo.fileservice.entities.Users
import org.springframework.data.jpa.repository.JpaRepository

interface UsersRepository : JpaRepository<Users, Long> {
    fun findByEmail(email: String): Users?
}