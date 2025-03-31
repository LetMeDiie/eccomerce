package kz.amihady.eccomerce.kafka.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {
    private final KafkaTopicsProperties kafkaTopicsProperties;

    @Bean
    public NewTopic imageAddedTopic(){
        return TopicBuilder
                .name(kafkaTopicsProperties.getImageAdded())
                .build();
    }

    @Bean
    public NewTopic imageDeletedTopic(){
        return TopicBuilder
                .name(kafkaTopicsProperties.getImageDeleted())
                .build();
    }


}
