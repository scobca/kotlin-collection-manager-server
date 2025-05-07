package org.itmo.invokerservice.storages

import org.itmo.invokerservice.services.dto.CommandInfoDto
import org.springframework.stereotype.Component

@Component
class CommandsStorage {
    private var commandsList: MutableMap<String, String>? = mutableMapOf()

    fun setCommands(commands: List<CommandInfoDto>) {
        commands.forEach { cmd ->
            commandsList?.set(cmd.endpoint, cmd.description)
        }
    }

    fun getAllCommands(): MutableMap<String, String>? = this.commandsList

    fun getCommandByName(name: String): Boolean {
        return commandsList?.get(name) != null
    }

    fun getCommandDescription(name: String): String? {
        return if (commandsList?.get(name) != null) {
            commandsList?.get(name)
        } else {
            null
        }
    }
}