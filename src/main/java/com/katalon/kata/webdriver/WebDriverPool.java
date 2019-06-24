package com.katalon.kata.webdriver;

import com.katalon.kata.helper.LogHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static com.katalon.kata.helper.Constants.SELENIUM_SERVER_URI;

public class WebDriverPool {

  private static final Logger log = LogHelper.getLogger();

  public static final String CHROME = "chrome";

  public static final String FIREFOX = "firefox";

  public static final String EDGE = "edge";

  public static final String INTERNET_EXPLORER = "internet_explorer";

  public static final String SAFARI = "safari";

  private Map<String, WebDriverFactory> factories;

  private ThreadLocal<Map<String, EventFiringWebDriver>> driverCache;

  private List<EventFiringWebDriver> allDrivers;

  private Consumer<EventFiringWebDriver> afterCreateDriverListener = w -> {};

  private static class WebDriverPoolSingleton {
    private static final WebDriverPool INSTANCE = new WebDriverPool();
  }

  public static WebDriverPool get() {
    return WebDriverPoolSingleton.INSTANCE;
  }

  private WebDriverPool() {
    initDefaultFactories();
    initDriverCache();
  }

  private void initDefaultFactories() {
    factories = new ConcurrentHashMap<>();
    factories.put(CHROME, new AbstractWebDriverFactory() {
      @Override
      protected DesiredCapabilities createDesiredCapabilities() {
        return DesiredCapabilities.chrome();
      }
    });
    factories.put(FIREFOX, new AbstractWebDriverFactory() {
      @Override
      protected DesiredCapabilities createDesiredCapabilities() {
        return DesiredCapabilities.firefox();
      }
    });
    factories.put(EDGE, new AbstractWebDriverFactory() {
      @Override
      protected DesiredCapabilities createDesiredCapabilities() {
        return DesiredCapabilities.edge();
      }
    });
    factories.put(INTERNET_EXPLORER, new AbstractWebDriverFactory() {
      @Override
      protected DesiredCapabilities createDesiredCapabilities() {
        return DesiredCapabilities.internetExplorer();
      }
    });
    factories.put(SAFARI, new AbstractWebDriverFactory() {
      @Override
      protected DesiredCapabilities createDesiredCapabilities() {
        return DesiredCapabilities.safari();
      }
    });
  }

  private void initDriverCache() {
    driverCache = new InheritableThreadLocal<>();
    allDrivers = new ArrayList<>();
  }

  private boolean hasQuit(EventFiringWebDriver driver) {
    SessionId sessionId = ((RemoteWebDriver) driver.getWrappedDriver()).getSessionId();
    return sessionId == null;
  }

  private void addToCache(String name, EventFiringWebDriver driver) {
    Map<String, EventFiringWebDriver> localThreadDrivers = driverCache.get();
    localThreadDrivers.put(name, driver);
    allDrivers.add(driver);
  }

  private void removeFromCache(String name, EventFiringWebDriver driver) {
    Map<String, EventFiringWebDriver> localThreadDrivers = driverCache.get();
    localThreadDrivers.remove(name);
    allDrivers.remove(driver);
  }

  public void setFactory(String name, WebDriverFactory factory) {
    factories.put(name, factory);
  }

  public void setAfterCreateDriverListener(Consumer<EventFiringWebDriver> afterCreateDriverListener) {
    this.afterCreateDriverListener = afterCreateDriverListener;
  }

  public WebDriver getDriver(String name, String seleniumServer) {

    log.info("Initializing WebDriver for: {}", name);
    Map<String, EventFiringWebDriver> localThreadDrivers = driverCache.get();
    if (localThreadDrivers == null) {
      localThreadDrivers = new HashMap<>();
      driverCache.set(localThreadDrivers);
    }
    EventFiringWebDriver driver = localThreadDrivers.get(name);
    boolean needsCreating;
    if (driver == null) {
      needsCreating = true;
    } else {
      if (hasQuit(driver)) {
        removeFromCache(name, driver);
        needsCreating = true;
      } else {
        needsCreating = false;
      }
    }
    if (needsCreating) {
      log.info("WebDriver cache is empty, creating new WebDriver.");
      WebDriverFactory factory = factories.get(name);
      RemoteWebDriver remoteWebDriver = factory.createRemoteWebDriver(seleniumServer + SELENIUM_SERVER_URI);
      driver = new EventFiringWebDriver(remoteWebDriver);
      afterCreateDriverListener.accept(driver);
      addToCache(name, driver);
    }
    return driver;
  }

  public void quitAll() {
    allDrivers.forEach(EventFiringWebDriver::quit);
  }
}
