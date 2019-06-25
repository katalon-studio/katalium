package com.katalon.kata.helper;

import org.slf4j.Logger;

import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class ParameterHelper {

    private static final Logger log = LogHelper.getLogger();

    private static Properties properties;

    static {
        try {
            loadParameters(Constants.KATA_DEFAULT_PARAMETERS_FILE);
        } catch (Exception e) {
            ExceptionHelper.rethrow(e);
        }
    }

    private static void loadParameters(String propertiesFile) {
        try (InputStream inputStream = ParameterHelper.class.getClassLoader().getResourceAsStream(propertiesFile)) {
            properties = new Properties();
            properties.load(inputStream);
        } catch (Exception ex) {
            log.error("Fail to load properties", ex);
        }
        properties.putAll(System.getProperties());
        properties.putAll(System.getenv());
    }

    public static String getParameterDefaultValue(String key) {
        String value = properties.getProperty(key);
        return value;
    }

    public static Set<String> getParameterNames() {
        return properties.stringPropertyNames();
    }
}
