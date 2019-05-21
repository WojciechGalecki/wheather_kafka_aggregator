package wg.weather.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
@ConfigurationProperties("kafka.topics")
public class KafkaTopicsProperties {
    private String weather;
}
