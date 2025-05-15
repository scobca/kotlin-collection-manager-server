package org.itmo.fileservice.repositories

import org.itmo.fileservice.entities.Coordinates
import org.springframework.data.jpa.repository.JpaRepository

interface CoordinatesRepository : JpaRepository<Coordinates, Long>