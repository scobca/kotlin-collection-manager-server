package org.itmo.collectionservice.config.dto

data class KafkaSystemMessage(
    val theme: String,
    val service: String,
    val message: String?,
)
