package wg.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class WeatherDataProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeatherDataProducerApplication.class, args);
    }
}
