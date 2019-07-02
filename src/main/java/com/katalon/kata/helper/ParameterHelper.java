package com.katalon.kata.helper;

import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class ParameterHelper {

    private static final Logger log = LogHelper.getLogger();

    private static Properties properties;

    static {
        try {
            loadParameters();
        } catch (Exception e) {
            ExceptionHelper.rethrow(e);
        }
    }

    private static void loadParameters() {
        properties = new Properties();
        String filePath = Constants.FRAMEWORK_PROPERTIES_FILE;
        try (InputStream inputStream = new FileInputStream(filePath)) {
            properties.load(inputStream);
        } catch (Exception ex) {
            log.error("Fail to load properties from {}", filePath, ex);
        }
        filePath = Constants.KATA_DEFAULT_PARAMETERS_FILE;
        try (InputStream inputStream = ParameterHelper.class.getClassLoader().getResourceAsStream(filePath)) {
            properties.load(inputStream);
        } catch (Exception ex) {
            log.error("Fail to load properties from {}", filePath, ex);
        }
        properties.putAll(System.getenv());
        properties.putAll(System.getProperties());
    }

    public static String getParameterDefaultValue(String key) {
        String value = properties.getProperty(key);
        return value;
    }

    public static Set<String> getParameterNames() {
        return properties.stringPropertyNames();
    }
}
