package com.katalon.kata.selenium;

import com.katalon.kata.helper.LogHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;

import java.util.function.Function;

public class PageTemplate {

  protected static final Logger log = LogHelper.getLogger();

  protected WebDriver driver;

  public void init(WebDriver driver) {
    log.info("Initializing Page: {}", this.getClass().getName());
    this.driver = driver;
    initElements(driver);
  }

  public void initElements(WebDriver driver) {
    PageFactory.initElements(driver, this);
  }

  protected void waitUntil(Function<? super WebDriver, ? extends Object> isTrue) {
    waitUntil(isTrue, 10);
  }

  protected void waitUntil(Function<? super WebDriver, ? extends Object> isTrue, long timeOutInSeconds) {
    WebDriverWait webDriverWait = new WebDriverWait(driver, timeOutInSeconds);
    webDriverWait.until(isTrue);
  }

  /**
   * @deprecated There was typo in the method name. Use `waitUntil` instead.
   * @param isTrue
   */
  @Deprecated
  protected void waitUtil(Function<? super WebDriver, ? extends Object> isTrue) {
    waitUntil(isTrue);
  }

  /**
   * @deprecated There was typo in the method name. Use `waitUntil` instead.
   * @param isTrue
   */
  protected void waitUtil(Function<? super WebDriver, ? extends Object> isTrue, long timeOutInSeconds) {
    waitUntil(isTrue, timeOutInSeconds);
  }
}