package com.katalon.kata.testng;

import com.katalon.kata.helper.LogHelper;
import com.katalon.kata.webdriver.WebDriverPool;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;

public class TestHelper {

  private static final Logger log = LogHelper.getLogger();

  public static WebDriver getDriver(String browserName, String seleniumServer) {
    log.info("Initializing WebDriver from context.");
    WebDriverPool pool = WebDriverPool.get();

    WebDriver driver = pool.getDriver(browserName, seleniumServer);
    return driver;
  }
}
