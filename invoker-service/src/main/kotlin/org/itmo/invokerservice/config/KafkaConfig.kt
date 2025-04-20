package org.itmo.invokerservice.config

import org.apache.kafka.clients.admin.NewTopic
import org.itmo.invokerservice.kafka.enums.KafkaEvents
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaConfig {
    @Bean
    fun systemTopic(): NewTopic {
        return TopicBuilder
            .name(KafkaEvents.SYSTEM.name)
            .partitions(3)
            .replicas(1)
            .build()
    }

    @Bean
    fun commandsSynchronization(): NewTopic {
        return TopicBuilder
            .name(KafkaEvents.COMMANDS_SYNCHRONIZATION.name)
            .partitions(3)
            .replicas(1)
            .build()
    }
}