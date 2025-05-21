package org.itmo.collectionservice.strategies

import org.itmo.collectionservice.config.kafka.KafkaCommandsSynchronizationProducer
import org.itmo.collectionservice.config.kafka.KafkaSystemProducer
import org.itmo.collectionservice.kafka.dto.KafkaCommandsSynchronizationDto
import org.itmo.collectionservice.kafka.dto.KafkaSystemMessageDto
import org.itmo.collectionservice.kafka.enums.KafkaServices
import org.itmo.collectionservice.kafka.enums.KafkaSystemThemes
import org.itmo.collectionservice.services.CommandEndpointService
import org.itmo.collectionservice.storages.CommandsStorage
import org.springframework.stereotype.Component

@Component
class StartupStrategy(
    private val systemProducer: KafkaSystemProducer,
    private val commandsProducer: KafkaCommandsSynchronizationProducer,
    private val commandsStorage: CommandsStorage,
    private val commandEndpointService: CommandEndpointService,
) {
    fun applicationStart() {
        systemProducer.sendEvent(
            KafkaSystemMessageDto(
                KafkaSystemThemes.SERVICE_STARTED,
                KafkaServices.COLLECTION_SERVICE,
                null,
            )
        )
    }

    fun sendCommandsToOtherServices() {
        commandEndpointService.getApiEndpoint()
        commandsProducer.sendEvent(KafkaCommandsSynchronizationDto(commandsStorage.getAllCommands()))
    }
}