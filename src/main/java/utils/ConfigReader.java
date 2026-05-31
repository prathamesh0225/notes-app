package utils;

import io.github.cdimascio.dotenv.Dotenv;

public class ConfigReader {

    private static final Dotenv dotenv =
            Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

    public static String get(String key) {

        String value =
                dotenv.get(key);

        if (value == null ||
                value.isBlank()) {

            throw new RuntimeException(
                    "Missing key: " + key
            );
        }

        return value;
    }
}
