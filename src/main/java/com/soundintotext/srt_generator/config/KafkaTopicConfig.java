package com.soundintotext.srt_generator.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic srtGenerationRequestsTopic() {
        return TopicBuilder.name("srt-generation-requests")
                .partitions(3)
                .replicas(1)
                .build();
    }
}