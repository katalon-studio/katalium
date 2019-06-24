package com.katalon.kata.webdriver;

import org.openqa.selenium.remote.RemoteWebDriver;

public interface WebDriverFactory {

  RemoteWebDriver createRemoteWebDriver(String seleniumServer);
}