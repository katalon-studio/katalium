package com.katalon.kata.testng;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.katalon.kata.helper.*;
import com.katalon.kata.selenium.PageTemplate;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.util.Strings;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.katalon.kata.helper.Constants.*;

public abstract class TestTemplate {

    protected static final Logger log = LogHelper.getLogger();

    private static final String SERVER_CONFIG_URI = "/ConfigSessionsServlet";

    private static final ObjectMapper objectMapper = JsonHelper.objectMapper();

    protected WebDriver driver;

    @BeforeMethod
    public void beforeTest(ITestContext context) {

        try {

            log.info("Initializing parameters.");

            Map<String, String> parameters = context.getCurrentXmlTest().getAllParameters();

            ParameterHelper.getParameterNames().forEach(key -> {
                String value = parameters.get(key);
                if (Strings.isNullOrEmpty(value)) {
                    value = ParameterHelper.getParameterDefaultValue(key);
                    parameters.put(key, value);
                }
            });

            String key;
            String value;

            key = KATA_BROWSER_NAME_PARAMETER;
            value = parameters.get(key);
            if (Strings.isNullOrEmpty(value)) {
                value = DEFAULT_BROWSER;
                parameters.put(key, value);
            }

            key = KATA_SELENIUM_SERVER_PARAMETER;
            value = parameters.get(key);
            if (Strings.isNullOrEmpty(value)) {
                value = DEFAULT_KATA_SELENIUM_SERVER;
                parameters.put(key, value);
            }

            String name = parameters.get(KATA_BROWSER_NAME_PARAMETER);
            String seleniumServer = parameters.get(KATA_SELENIUM_SERVER_PARAMETER);

            log.info("Initializing WebDriver {} {}", name, seleniumServer);
            driver = TestHelper.getDriver(name, seleniumServer);

            RemoteWebDriver remoteWebDriver = (RemoteWebDriver) ((EventFiringWebDriver) driver).getWrappedDriver();
            SessionId sessionId = remoteWebDriver.getSessionId();

            String screenshot = parameters.get(KATA_DISABLE_SCREENSHOT_PARAMETER);
            String url = parameters.get(KATA_SELENIUM_SERVER_PARAMETER) + SELENIUM_SERVER_ADMIN_URI;
            TestConfiguration testConfiguration = new TestConfiguration();
            testConfiguration.setSessionId(sessionId.toString());
            testConfiguration.setDisableScreenshot(Boolean.valueOf(screenshot));
            updateTestConfig(url, testConfiguration);

            log.info("Initializing Pages.");
            initPages();

        } catch (Exception e) {
            ExceptionHelper.rethrow(e);
        }
    }

    private void updateTestConfig(String url, TestConfiguration testConfiguration) {
        url += SERVER_CONFIG_URI;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);

            HttpPost httpPost = new HttpPost(uriBuilder.build());

            String requestContent = objectMapper.writeValueAsString(testConfiguration);

            HttpHelper.sendRequest(
                    httpPost,
                    null,
                    null,
                    null,
                    IOUtils.toInputStream(requestContent, StandardCharsets.UTF_8),
                    null,
                    null
            );

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot update test configuration: ", e);
        }
    }

    private void initPages() throws IllegalArgumentException, IllegalAccessException, InstantiationException {
        List<PageTemplate> pages = new ArrayList<>();
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            Class<?> fieldType = field.getType();
            if (PageTemplate.class.isAssignableFrom(fieldType)) {
                field.setAccessible(true);
                PageTemplate page = (PageTemplate) field.get(this);
                if (page == null) {
                    page = (PageTemplate) fieldType.newInstance();
                    field.set(this, page);
                }
                field.setAccessible(false);
                log.info("{}", page);
                pages.add(page);
            }
        }
        pages.forEach(page -> page.init(driver));
    }
}
