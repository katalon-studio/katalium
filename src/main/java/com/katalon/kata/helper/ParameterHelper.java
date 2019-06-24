package com.katalon.kata.helper;

import org.slf4j.Logger;

import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class ParameterHelper {

    private static final Logger log = LogHelper.getLogger();

    private static Properties properties;

    private ParameterHelper() {
        loadParameters(Constants.KATA_DEFAULT_PARAMETERS_FILE);
    }

    private void loadParameters(String propertiesFile) {
        try (InputStream inputStream = ParameterHelper.class.getClassLoader().getResourceAsStream(propertiesFile)) {
            properties = new Properties();
            properties.load(inputStream);
        } catch (Exception ex) {
            log.error("Fail to load properties", ex);
        }
        properties.putAll(System.getProperties());
        properties.putAll(System.getenv());
    }

    public String getParameterDefaultValue(String key) {
        String value = properties.getProperty(key);
        return value;
    }

    public Set<String> getPropertyNames() {
        return properties.stringPropertyNames();
    }

    public static ParameterHelper getInstance() {
        return ParameterHelperHolder.INSTANCE;
    }

    private static class ParameterHelperHolder {

        private static final ParameterHelper INSTANCE = new ParameterHelper();

    }
}
