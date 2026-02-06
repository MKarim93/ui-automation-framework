package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties props = new Properties();

    static {
        try (InputStream is = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config/config.properties")) {

            if (is == null) {
                throw new RuntimeException("Could not find config/config.properties in test resources");
            }

            props.load(is);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config properties", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String val = props.getProperty(key);
        return (val == null) ? defaultValue : Boolean.parseBoolean(val.trim());
    }

    public static int getInt(String key, int defaultValue) {
        String val = props.getProperty(key);
        if (val == null) return defaultValue;
        return Integer.parseInt(val.trim());
    }
}
