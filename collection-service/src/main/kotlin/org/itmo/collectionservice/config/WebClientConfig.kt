package org.itmo.collectionservice.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Value("\${config.file-service.host}")
    private lateinit var fileServiceConfig: String

    @Value("\${config.invoker-service.host}")
    private lateinit var invokerServiceHost: String

    @Bean
    @Qualifier(value = "fileService")
    fun fileServiceWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl(fileServiceConfig)
            .build()
    }

    @Bean
    @Qualifier(value = "fileService")
    fun invokerServiceWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl(invokerServiceHost)
            .build()
    }
}