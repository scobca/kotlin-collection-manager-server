package org.itmo.collectionservice.config.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class KafkaRequireSystemSynchronizationMessage(
    @JsonProperty("message") val message: String?
)
