package utils;

import java.util.Map;

public class ConfigReader {

    private static final Map<String,String> keys =
            Map.of(
                    "baseUrl","BASE_URL",
                    "apiBaseUrl","API_BASE_URL",
                    "browser","BROWSER",
                    "email","NOTES_EMAIL",
                    "password","NOTES_PASSWORD",
                    "gemini.api.key",
                    "GEMINI_API_KEY"
            );

    public static String get(String key) {

        String env =
                keys.get(key);

        String value =
                System.getenv(env);

        if (value == null ||
                value.isBlank()) {

            throw new RuntimeException(
                    "Missing env variable: "
                            + env
            );
        }

        return value;
    }
}
