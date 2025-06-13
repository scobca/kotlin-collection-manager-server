package org.itmo.fileservice.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.TimeZoneStorage
import org.hibernate.annotations.TimeZoneStorageType
import org.itmo.fileservice.collection.items.Furnish
import org.itmo.fileservice.io.BasicSuccessfulResponse
import java.time.ZonedDateTime

@Entity
@Table(name = "flats")
data class Flats(
    @Id
    @Column(nullable = false)
    val id: Long,

    @Column(nullable = false)
    var name: String,

    @OneToOne(
        fetch = FetchType.LAZY,
        optional = false,
        cascade = [CascadeType.ALL]
    )
    @JoinColumn(name = "coordinates_id", referencedColumnName = "id")
    val coordinates: Coordinates,

    @Column(nullable = false)
    var area: Long,

    @Column(name = "number_of_rooms", nullable = false)
    var numberOfRooms: Long,

    @Column(nullable = false)
    var price: Long,

    @Column(nullable = false)
    var balcony: Boolean,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var furnish: Furnish,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "house_id", nullable = false)
    val house: Houses,

    @JsonIgnoreProperties(value = ["password"])
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: Users,

    @JsonIgnore
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    @Column(nullable = false)
    val createdAt: ZonedDateTime,
) : Convertable<Flats> {
    override fun toHttpResponse(): BasicSuccessfulResponse<Flats> = BasicSuccessfulResponse(this)
}
