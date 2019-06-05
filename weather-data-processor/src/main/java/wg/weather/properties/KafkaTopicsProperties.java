package wg.weather.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties("kafka.topics")
public class KafkaTopicsProperties {
    private String weather;
    private String fromPoland;
    private String highTemp;
}
