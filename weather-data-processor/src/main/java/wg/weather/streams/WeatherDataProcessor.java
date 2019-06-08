package wg.weather.streams;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import wg.weather.model.WeatherModel;
import wg.weather.properties.KafkaStreamsProperties;
import wg.weather.properties.KafkaTopicsProperties;

@Slf4j
@Component
public class WeatherDataProcessor {
    private static final double ZERO_CELSIUS_IN_KELWIN = 273.15;
    private static final double THIRTY_CELSIUS_IN_KELWIN = 303.15;
    private static final float FIFTEEN_METERS_PER_SEC = 15.0f;
    private static final String KAFKA_SENDING_LOG_MSG = "Sending weather data for city {} to {} topic ...";

    @Autowired
    private KafkaTopicsProperties kafkaTopicsProperties;

    @Autowired
    private KafkaStreamsProperties kafkaStreamsProperties;

    @PostConstruct
    public void processData() {
        log.info("Starting process weather data...");
        KafkaStreams kafkaStreams = createTopology(kafkaStreamsProperties.getKafkaStreamsConfig());

        kafkaStreams.cleanUp();
        kafkaStreams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }

    private KafkaStreams createTopology(Properties config) {
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, WeatherModel> weatherData = builder.stream(kafkaTopicsProperties.getWeather());

        KStream<String, WeatherModel>[] branches = weatherData.branch(
            (k, data) -> isTempBelowZeroCelsius(data),
            (k, data) -> isTempOver30Celsius(data),
            (k, data) -> isWindSpeedOver15MetersPerSec(data)
        );

        sendAndLogStreamBranchesData(branches);

        return new KafkaStreams(builder.build(), config);
    }

    private boolean isTempBelowZeroCelsius(WeatherModel data) {
        return getTemp(data) < ZERO_CELSIUS_IN_KELWIN;
    }

    private boolean isTempOver30Celsius(WeatherModel data) {
        return getTemp(data) > THIRTY_CELSIUS_IN_KELWIN;
    }

    private boolean isWindSpeedOver15MetersPerSec(WeatherModel data) {
        return getWindSpeed(data) > FIFTEEN_METERS_PER_SEC;
    }

    private Double getTemp(WeatherModel data) {
        return data.getMain().getTemp();
    }

    private Float getWindSpeed(WeatherModel data) {
        return data.getWind().getSpeed();
    }

    private void sendAndLogStreamBranchesData(KStream<String, WeatherModel>[] branches) {
        KStream<String, WeatherModel> tempBelowZeroCelsius = branches[0];
        KStream<String, WeatherModel> tempOver30Celsius = branches[1];
        KStream<String, WeatherModel> windSpeedOver15ms = branches[2];

        final String HIGH_TEMP_TOPIC = kafkaTopicsProperties.getHighTemp();
        final String LOW_TEMP_TOPIC = kafkaTopicsProperties.getLowTemp();
        final String STRONG_WIND_TOPIC = kafkaTopicsProperties.getStrongWind();

        tempBelowZeroCelsius.peek((k, data) -> log.info(KAFKA_SENDING_LOG_MSG, k, HIGH_TEMP_TOPIC)).to(HIGH_TEMP_TOPIC);

        tempOver30Celsius.peek((k, data) -> log.info(KAFKA_SENDING_LOG_MSG, k, LOW_TEMP_TOPIC)).to(LOW_TEMP_TOPIC);

        windSpeedOver15ms.peek((k, data) -> log.info(KAFKA_SENDING_LOG_MSG, k, STRONG_WIND_TOPIC)).to(STRONG_WIND_TOPIC);
    }
}
