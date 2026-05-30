package utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties props = new Properties();
    static {
        try {
            InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties.example");
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String key) {
        String env = System.getenv(key.toUpperCase().replace(".", "_"));
        return env != null ? env : props.getProperty(key);
    }
}
