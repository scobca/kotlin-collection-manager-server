package org.itmo.fileservice.config.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.itmo.fileservice.config.enums.SystemServices
import org.itmo.fileservice.config.enums.SystemThemes

data class KafkaSystemMessage(
    @JsonProperty("theme") val theme: SystemThemes,
    @JsonProperty("service") val service: SystemServices,
    @JsonProperty("message") val message: String?,
)
