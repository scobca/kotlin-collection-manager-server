package org.itmo.collectionservice.services

import org.itmo.collectionservice.annotations.ApiEndpoint
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@Service
class ApiEndpointService(private val applicationContext: ApplicationContext) {
    fun getApiEndpoint(): List<String> {
        val endpoints = mutableListOf<String>()
        val controllers = applicationContext.getBeansWithAnnotation(RestController::class.java)

        controllers.values.forEach { controller ->
            controller.javaClass.methods.forEach { method ->
                if (method.isAnnotationPresent(ApiEndpoint::class.java)) {
                    when {
                        method.isAnnotationPresent(GetMapping::class.java) -> {
                            val annotation = method.getAnnotation(GetMapping::class.java)
                            endpoints.add(annotation.value.first().substring(1))
                        }

                        method.isAnnotationPresent(PostMapping::class.java) -> {
                            val annotation = method.getAnnotation(PostMapping::class.java)
                            endpoints.add(annotation.value.first().substring(1))
                        }

                        method.isAnnotationPresent(PatchMapping::class.java) -> {
                            val annotation = method.getAnnotation(PatchMapping::class.java)
                            endpoints.add(annotation.value.first().substring(1))
                        }

                        method.isAnnotationPresent(DeleteMapping::class.java) -> {
                            val annotation = method.getAnnotation(DeleteMapping::class.java)
                            endpoints.add(annotation.value.first().substring(1))
                        }

                        else -> null
                    }
                }
            }
        }

        return endpoints
    }
}