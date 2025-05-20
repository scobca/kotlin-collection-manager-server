package org.itmo.fileservice.dto.collection.house


data class UpdateHouseDto(val id: Long, val name: String?, val year: Int?, val numberOfFloors: Long?)
