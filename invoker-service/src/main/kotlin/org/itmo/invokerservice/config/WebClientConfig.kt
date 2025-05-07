package org.itmo.invokerservice.config

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    private val dotenv: Dotenv = Dotenv.load()

    @Bean
    @Qualifier(value = "collectionService")
    fun invokerServiceWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl(dotenv.get("COLLECTION_SERVICE_URL"))
            .build()
    }
}