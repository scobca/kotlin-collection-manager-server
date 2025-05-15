package org.itmo.invokerservice.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Value("\${config.collection-service-host}")
    private lateinit var collectionServiceConfig: String

    @Bean
    @Qualifier(value = "collectionService")
    fun invokerServiceWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl(collectionServiceConfig)
            .build()
    }
}