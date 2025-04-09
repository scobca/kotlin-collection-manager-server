package org.itmo.collectionservice.services

import org.itmo.collectionservice.annotations.CommandDescription
import org.itmo.collectionservice.annotations.CommandEndpoint
import org.itmo.collectionservice.services.dto.CommandInfoDto
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@Service
class CommandEndpointService(private val applicationContext: ApplicationContext) {
    fun getApiEndpoint(): MutableList<CommandInfoDto> {
        val endpoints = mutableListOf<CommandInfoDto>()
        val controllers = applicationContext.getBeansWithAnnotation(RestController::class.java)

        controllers.values.forEach { controller ->
            controller.javaClass.methods.forEach { method ->
                if (
                    method.isAnnotationPresent(CommandEndpoint::class.java) && method.isAnnotationPresent(CommandDescription::class.java)
                ) {
                    when {
                        method.isAnnotationPresent(GetMapping::class.java) -> {
                            val commandEndpoint = method.getAnnotation(GetMapping::class.java)
                            val description = method.getAnnotation(CommandDescription::class.java).description
                            endpoints.add(CommandInfoDto(commandEndpoint.value.first().substring(1), description))
                        }

                        method.isAnnotationPresent(PostMapping::class.java) -> {
                            val commandEndpoint = method.getAnnotation(PostMapping::class.java)
                            val description = method.getAnnotation(CommandDescription::class.java).description
                            endpoints.add(CommandInfoDto(commandEndpoint.value.first().substring(1), description))
                        }

                        method.isAnnotationPresent(PatchMapping::class.java) -> {
                            val commandEndpoint = method.getAnnotation(PatchMapping::class.java)
                            val description = method.getAnnotation(CommandDescription::class.java).description
                            endpoints.add(CommandInfoDto(commandEndpoint.value.first().substring(1), description))
                        }

                        method.isAnnotationPresent(DeleteMapping::class.java) -> {
                            val commandEndpoint = method.getAnnotation(DeleteMapping::class.java)
                            val description = method.getAnnotation(CommandDescription::class.java).description
                            endpoints.add(CommandInfoDto(commandEndpoint.value.first().substring(1), description))
                        }

                        else -> null
                    }
                }
            }
        }

        return endpoints
    }
}