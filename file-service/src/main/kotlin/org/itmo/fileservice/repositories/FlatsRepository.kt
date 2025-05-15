package org.itmo.fileservice.repositories

import org.itmo.fileservice.entities.Flats
import org.springframework.data.jpa.repository.JpaRepository

interface FlatsRepository : JpaRepository<Flats, Long>