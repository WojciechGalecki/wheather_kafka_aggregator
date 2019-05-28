package wg.weather.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("api")
public class ApiProperties {
    private String id;
    private String url;
}
