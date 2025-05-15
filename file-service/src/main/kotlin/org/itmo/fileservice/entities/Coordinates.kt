package org.itmo.fileservice.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.itmo.fileservice.io.BasicSuccessfulResponse

@Entity
@Table(name = "coordinates")
data class Coordinates(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false)
    val x: Long,

    @Column(nullable = false)
    val y: Float,
) : Convertable<Coordinates> {
    override fun toHttpResponse(): BasicSuccessfulResponse<Coordinates> = BasicSuccessfulResponse(this)
}