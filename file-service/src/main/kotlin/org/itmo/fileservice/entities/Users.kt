package org.itmo.fileservice.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.itmo.fileservice.io.BasicSuccessfulResponse

@Entity
@Table(name = "users")
data class Users(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = true)
    var password: String,
) : Convertable<Users> {
    override fun toHttpResponse(): BasicSuccessfulResponse<Users> = BasicSuccessfulResponse<Users>(this)
}