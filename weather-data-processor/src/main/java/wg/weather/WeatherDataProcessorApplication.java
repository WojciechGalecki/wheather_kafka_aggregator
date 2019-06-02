package wg.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class WeatherDataProcessorApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeatherDataProcessorApplication.class, args);
    }
}
