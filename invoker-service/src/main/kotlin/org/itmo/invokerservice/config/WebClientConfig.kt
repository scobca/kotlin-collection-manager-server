package org.itmo.invokerservice.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Value("\${config.collection-service.host}")
    private lateinit var collectionServiceConfig: String

    @Value("\${config.file-service.host}")
    private lateinit var fileServiceHost: String

    @Bean
    @Qualifier(value = "collectionService")
    fun collectionServiceWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl(collectionServiceConfig)
            .build()
    }

    @Bean
    @Qualifier(value = "fileService")
    fun fileServiceWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl(fileServiceHost)
            .build()
    }
}