package com.katalon.kata.helper;

import static com.katalon.kata.webdriver.WebDriverPool.CHROME;

public class Constants {

  // parameters

  public static final String KATA_DEFAULT_PARAMETERS_FILE = "kata-default.properties";

  // parameter keys

  public static final String KATA_BROWSER_NAME_PARAMETER = "kataBrowser";

  public static final String KATA_SELENIUM_SERVER_PARAMETER = "kataSeleniumServer";

  public static final String KATA_DISABLE_SCREENSHOT_PARAMETER = "kataDisableScreenshot";

  public static final String KATA_PROJECT_ID_PARAMETER = "kataProjectId";

  // default value

  public static final String DEFAULT_BROWSER = CHROME;

  public static final String DEFAULT_KATA_SELENIUM_SERVER = "http://localhost:4444";

  // server

  public static final String SELENIUM_SERVER_URI = "/wd/hub";

  public static final String SELENIUM_SERVER_ADMIN_URI = "/grid/admin";
}
