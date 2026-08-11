package com.automation.configuration;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                properties.load(in);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key){
        // System property overrides file, then environment variable (uppercase)
        String sys = System.getProperty(key);
        if (sys != null) return sys;
        String env = System.getenv(key.toUpperCase());
        if (env != null) return env;
        return properties.getProperty(key);
    }
}
