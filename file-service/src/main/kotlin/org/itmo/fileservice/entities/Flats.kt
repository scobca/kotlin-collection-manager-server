package org.itmo.fileservice.entities

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.TimeZoneStorage
import org.hibernate.annotations.TimeZoneStorageType
import org.itmo.fileservice.collection.items.Furnish
import java.time.ZonedDateTime

@Entity
@Table(name = "flats")
data class Flats(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false)
    val name: String,

    @OneToOne(
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(name = "coordinates_id", referencedColumnName = "id")
    val coordinates: Coordinates,

    @Column(nullable = false)
    val area: Long,

    @Column(name = "number_of_rooms", nullable = false)
    val numberOfRooms: Long,

    @Column(nullable = false)
    val price: Long,

    @Column(nullable = false)
    val balcony: Boolean,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val furnish: Furnish,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id", nullable = false)
    val house: Houses,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: Users,

    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    @Column(nullable = false)
    val createdAt: ZonedDateTime,
)
