package org.itmo.collectionservice.storages

import org.itmo.collectionservice.services.dto.CommandInfoDto
import org.springframework.stereotype.Component

@Component
class CommandsStorage {
    private var commandsList: MutableList<CommandInfoDto> = mutableListOf<CommandInfoDto>()

    fun addCommand(command: CommandInfoDto) {
        commandsList.add(command)
    }

    fun getAllCommands(): MutableList<CommandInfoDto> = commandsList
}