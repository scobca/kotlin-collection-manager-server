package org.itmo.collectionservice.services

import org.itmo.collectionservice.annotations.CommandDescription
import org.itmo.collectionservice.annotations.CommandEndpoint
import org.itmo.collectionservice.services.dto.CommandInfoDto
import org.itmo.collectionservice.storages.CommandsStorage
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@Service
class CommandEndpointService(
    private val applicationContext: ApplicationContext,
    private val commandsStorage: CommandsStorage
) {
    fun getApiEndpoint() {
        val controllers = applicationContext.getBeansWithAnnotation(RestController::class.java)

        controllers.values
            .flatMap { controller ->
                controller.javaClass.methods.asSequence()
                    .filter { method ->
                        method.isAnnotationPresent(CommandEndpoint::class.java) &&
                                method.isAnnotationPresent(CommandDescription::class.java)
                    }
                    .mapNotNull { method ->
                        val description = method.getAnnotation(CommandDescription::class.java).description

                        val commandPath = when {
                            method.isAnnotationPresent(GetMapping::class.java) ->
                                method.getAnnotation(GetMapping::class.java).value.firstOrNull()
                            method.isAnnotationPresent(PostMapping::class.java) ->
                                method.getAnnotation(PostMapping::class.java).value.firstOrNull()
                            method.isAnnotationPresent(PatchMapping::class.java) ->
                                method.getAnnotation(PatchMapping::class.java).value.firstOrNull()
                            method.isAnnotationPresent(DeleteMapping::class.java) ->
                                method.getAnnotation(DeleteMapping::class.java).value.firstOrNull()
                            else -> null
                        }

                        commandPath?.let { path ->
                            CommandInfoDto(path.removePrefix("/"), description)
                        }
                    }
                    .toList()
            }
            .forEach { commandInfo ->
                commandsStorage.addCommand(commandInfo)
            }
    }

}