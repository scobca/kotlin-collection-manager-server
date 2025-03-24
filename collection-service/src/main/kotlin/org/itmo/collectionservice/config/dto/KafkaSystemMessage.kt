package org.itmo.collectionservice.config.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.itmo.collectionservice.config.enums.SystemServices
import org.itmo.collectionservice.config.enums.SystemThemes

data class KafkaSystemMessage(
    @JsonProperty("theme") val theme: SystemThemes,
    @JsonProperty("service") val service: SystemServices,
    @JsonProperty("message") val message: String?,
)
