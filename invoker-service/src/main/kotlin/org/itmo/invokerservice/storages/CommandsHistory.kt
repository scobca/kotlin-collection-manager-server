package org.itmo.invokerservice.storages

import org.springframework.stereotype.Component
import java.util.LinkedList

@Component
class CommandsHistory {
    val history: LinkedList<String> = LinkedList()

    fun getCommandsHistory(): LinkedList<String> = history

    fun addCommand(command: String) {
        if (history.size >= 10) {
            history.removeFirst()
        }
        history.add(command)
    }
}