package com.katalon.kata.webdriver;

import com.katalon.kata.helper.ExceptionHelper;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public abstract class AbstractWebDriverFactory implements WebDriverFactory {

  @Override
  public final RemoteWebDriver createRemoteWebDriver(String seleniumServer) {
    try {
      URL url = new URL(seleniumServer);
      DesiredCapabilities capabilities = createDesiredCapabilities();
      RemoteWebDriver driver = new RemoteWebDriver(url, capabilities);
      return driver;
    } catch (Exception e) {
      return ExceptionHelper.rethrow(e);
    }
  }

  protected abstract DesiredCapabilities createDesiredCapabilities();
}