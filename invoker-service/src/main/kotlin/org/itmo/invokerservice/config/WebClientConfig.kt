package org.itmo.invokerservice.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Bean
    @Qualifier(value = "collectionService")
    fun invokerServiceWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("http://localhost:8081")
            .build()
    }
}