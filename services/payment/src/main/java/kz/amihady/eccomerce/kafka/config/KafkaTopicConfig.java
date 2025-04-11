package kz.amihady.eccomerce.kafka.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Autowired
    private KafkaTopicsProperties kafkaTopicsProperties;

    @Bean
    public NewTopic paymentOrderTopic(){
        return TopicBuilder
                .name(kafkaTopicsProperties.getPaymentOrder())
                .build();
    }

    @Bean
    public NewTopic paymentInventoryTopic(){
        return TopicBuilder
                .name(kafkaTopicsProperties.getPaymentInventory())
                .build();
    }
}
