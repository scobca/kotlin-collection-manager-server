package org.itmo.collectionservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Bean
    fun fileServiceWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("http://localhost:8082")
            .build()
    }

    @Bean
    fun invokerServiceWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("http://localhost:8080")
            .build()
    }
}