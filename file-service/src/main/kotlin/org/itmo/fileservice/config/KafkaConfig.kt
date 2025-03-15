package org.itmo.fileservice.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaConfig {
    @Bean
    fun systemTopic(): NewTopic {
        return TopicBuilder.name("system")
            .partitions(1)
            .replicas(2)
            .build()
    }

    @Bean
    fun systemSynchronizationTopic(): NewTopic {
        return TopicBuilder.name("system-synchronization")
            .partitions(1)
            .replicas(2)
            .build()
    }

    @Bean
    fun errorTopic(): NewTopic {
        return TopicBuilder.name("error")
            .partitions(1)
            .replicas(2)
            .build()
    }

    @Bean
    fun commandRequestTopic(): NewTopic {
        return TopicBuilder.name("command-request")
            .partitions(1)
            .replicas(2)
            .build()
    }

    @Bean
    fun commandResponseTopic(): NewTopic {
        return TopicBuilder.name("command-response")
            .partitions(1)
            .replicas(2)
            .build()
    }

    @Bean
    fun collectionChangesTopic(): NewTopic {
        return TopicBuilder.name("collection-changes")
            .partitions(1)
            .replicas(2)
            .build()
    }
}