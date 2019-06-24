package com.katalon.kata.katalon;

import com.katalon.kata.helper.ExceptionHelper;
import com.katalon.kata.helper.LogHelper;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class KatalonProperties {

    private static final Map<String, String> configurations = new HashMap<>();

    private static final Logger log = LogHelper.getLogger();

    private static final String CONFIG_FILE_PATH = System.getProperty("user.home") + File.separator + ".katalon" + File.separator +"framework.properties";

    private static final String KATALON_SERVER_URL_KEY = "KATALON_SERVER_URL";

    private static final String EMAIL_KEY = "KATALON_EMAIL";

    private static final String PASSWORD_KEY = "KATALON_API_KEY";

    public static final String DEFAULT_KATALON_SERVER_URL = "https://analytics.katalon.com";

    private String serverApiUrl;

    // must be provided
    private String email;

    private String password;

    public KatalonProperties() {
        loadConfigurations();
        loadApplicationProperties();
    }

    private void loadApplicationProperties() {
        this.serverApiUrl = getConfiguration(KATALON_SERVER_URL_KEY, DEFAULT_KATALON_SERVER_URL);
        this.email = getConfiguration(EMAIL_KEY);
        this.password = getConfiguration(PASSWORD_KEY);
    }

    private void loadConfigurations() {
        Path path = Paths.get(CONFIG_FILE_PATH);
        File file = path.toFile();
        try (InputStream inputStream = new FileInputStream(file)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            properties.forEach((key, value) -> configurations.put((String) key, (String) value));
        } catch (IOException e) {
            log.info("Cannot read configuration from file: " + CONFIG_FILE_PATH);
            ExceptionHelper.rethrow(e);
        }
    }

    private String getConfiguration(String key) {
        return getConfiguration(key, null);
    }

    private String getConfiguration(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null) {
            value = configurations.get(key);
        }
        if (value == null) {
            value = defaultValue;
        }
        if (value == null) {
            String errorMessage = "Cannot get configuration for key: " + key;
            throw new IllegalStateException(errorMessage);
        }
        return value;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getServerApiUrl() {
        return serverApiUrl;
    }
}
