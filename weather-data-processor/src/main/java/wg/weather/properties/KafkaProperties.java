package wg.weather.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties("kafka")
public class KafkaProperties {
    private String broker;
    private String schemaRegistry;
}
