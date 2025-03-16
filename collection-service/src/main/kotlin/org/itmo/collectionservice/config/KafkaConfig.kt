package org.itmo.collectionservice.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaConfig {
    @Bean
    fun systemTopic(): NewTopic {
        return TopicBuilder.name("system")
            .partitions(3)
            .replicas(3)
            .build()
    }

    @Bean
    fun requireSystemSynchronizationTopic(): NewTopic {
        return TopicBuilder.name("require-system-synchronization")
            .partitions(3)
            .replicas(3)
            .build()
    }

    @Bean
    fun commandsSynchronizationTopic(): NewTopic {
        return TopicBuilder.name("commands-synchronization")
            .partitions(3)
            .replicas(3)
            .build()
    }

    @Bean
    fun collectionSynchronizationTopic(): NewTopic {
        return TopicBuilder.name("collection-synchronization")
            .partitions(3)
            .replicas(3)
            .build()
    }

    @Bean
    fun errorTopic(): NewTopic {
        return TopicBuilder.name("error")
            .partitions(3)
            .replicas(3)
            .build()
    }

    @Bean
    fun commandRequestTopic(): NewTopic {
        return TopicBuilder.name("command-request")
            .partitions(3)
            .replicas(3)
            .build()
    }

    @Bean
    fun commandResponseTopic(): NewTopic {
        return TopicBuilder.name("command-response")
            .partitions(3)
            .replicas(3)
            .build()
    }

    @Bean
    fun collectionChangesTopic(): NewTopic {
        return TopicBuilder.name("collection-changes")
            .partitions(3)
            .replicas(3)
            .build()
    }
}