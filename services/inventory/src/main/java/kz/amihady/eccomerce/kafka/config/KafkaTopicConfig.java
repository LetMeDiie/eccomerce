package kz.amihady.eccomerce.kafka.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {
    private final KafkaTopicsProperties kafkaTopicsProperties;

    public NewTopic inventoryUpdatedTopic(){
        return TopicBuilder
                .name(kafkaTopicsProperties.getInventoryUpdated())
                .build();
    }
}
