package org.itmo.fileservice.config

import org.itmo.fileservice.collection.Collection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CollectionConfig {
    @Bean
    fun collection(): Collection {
        return Collection()
    }

}