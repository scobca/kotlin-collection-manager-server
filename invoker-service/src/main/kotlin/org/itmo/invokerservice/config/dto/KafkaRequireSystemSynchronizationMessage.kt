package org.itmo.invokerservice.config.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class KafkaRequireSystemSynchronizationMessage(
    @JsonProperty("message") val message: String?
)
