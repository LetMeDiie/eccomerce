package kz.amihady.eccomerce.kafka.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka.topics")
@Getter
@Setter
public class KafkaTopicsProperties {
    private String orderCanceled;
    private String orderPaid;
    private String orderReserveRequest;
    private String orderReserveResponse;
}
