package com.katalon.kata.katalon;

import com.katalon.kata.helper.ExceptionHelper;
import com.katalon.kata.helper.LogHelper;
import com.katalon.kata.helper.ParameterHelper;
import org.apache.commons.lang3.StringUtils;
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

    private static final String KATALON_SERVER_URL_KEY = "KATALON_SERVER_URL";

    private static final String EMAIL_KEY = "KATALON_EMAIL";

    private static final String PASSWORD_KEY = "KATALON_API_KEY";

    private static final String DEFAULT_KATALON_SERVER_URL = "https://analytics.katalon.com";

    private String serverApiUrl;

    // must be provided
    private String email;

    private String password;

    public KatalonProperties() {
        loadApplicationProperties();
    }

    private void loadApplicationProperties() {
        this.serverApiUrl = ParameterHelper.getParameterDefaultValue(KATALON_SERVER_URL_KEY);
        if (StringUtils.isBlank(this.serverApiUrl)) {
            this.serverApiUrl = DEFAULT_KATALON_SERVER_URL;
        }
        this.email = ParameterHelper.getParameterDefaultValue(EMAIL_KEY);
        this.password = ParameterHelper.getParameterDefaultValue(PASSWORD_KEY);
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
