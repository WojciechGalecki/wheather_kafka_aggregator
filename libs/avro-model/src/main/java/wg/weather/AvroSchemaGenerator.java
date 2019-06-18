package wg.weather;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import org.apache.avro.reflect.ReflectData;

import wg.weather.model.WeatherData;

public class AvroSchemaGenerator {
    private static final String AVRO_SCHEMAS_DIR = "libs/avro-model/src/main/avro/";

    public static void main(String[] args) {
        String weatherDataSchema = getSchema(WeatherData.class);

        saveAvroSchemaToFile(weatherDataSchema, "weather-data.avsc");
    }

    private static String getSchema(Class clazz) {
        String schema = ReflectData.AllowNull.get().getSchema(clazz).toString(true);
        return schema
            .replace("wg.weather.model", "wg.weather.avro");
    }

    private static void saveAvroSchemaToFile(String avroSchema, String filename) {
        try {
            File file = new File(AVRO_SCHEMAS_DIR + filename);
            Writer writer = new OutputStreamWriter(new FileOutputStream(file), UTF_8);
            PrintWriter out = new PrintWriter(writer);
            out.println(avroSchema);
            out.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
