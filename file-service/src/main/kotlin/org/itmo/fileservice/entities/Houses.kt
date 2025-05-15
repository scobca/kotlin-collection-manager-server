package org.itmo.fileservice.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.itmo.fileservice.io.BasicSuccessfulResponse

@Entity
@Table(name = "houses")
data class Houses(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val year: Int,

    @Column(nullable = false)
    val numberOfFloors: Long,
) : Convertable<Houses> {
    override fun toHttpResponse(): BasicSuccessfulResponse<Houses> = BasicSuccessfulResponse(this)
}
