package kz.amihady.eccomerce.kafka.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class KafkaTopicConfig {
    private final KafkaTopicsProperties kafkaTopicsProperties;

    @Bean
    public NewTopic productCreatedTopic() {
        log.info("Создание топика: "+kafkaTopicsProperties.getProductCreated());
        return TopicBuilder
                .name(kafkaTopicsProperties.getProductCreated())
                .build();
    }

    @Bean
    public NewTopic productDeletedTopic() {
        log.info("Создание топика: "+kafkaTopicsProperties.getProductDeleted());
        return TopicBuilder
                .name(kafkaTopicsProperties.getProductDeleted())
                .build();
    }
}