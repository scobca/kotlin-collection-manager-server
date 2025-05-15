package org.itmo.fileservice.repositories

import org.itmo.fileservice.entities.Houses
import org.springframework.data.jpa.repository.JpaRepository

interface HousesRepository : JpaRepository<Houses, Long>