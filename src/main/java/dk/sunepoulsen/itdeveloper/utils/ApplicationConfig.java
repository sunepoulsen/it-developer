package dk.sunepoulsen.itdeveloper.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public class ApplicationConfig {
    @SneakyThrows
    public static Properties loadConfig() {
        try {
            Properties config = new Properties();
            config.load(ApplicationConfig.class.getResourceAsStream("/application.properties"));

            return config;
        }
        catch( IOException ex ) {
            log.error("Unable to load /application.properties: {}", ex.getMessage(), ex);
            throw ex;
        }
    }
}
