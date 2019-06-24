package com.katalon.kata.selenium;

import com.katalon.kata.helper.LogHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

public class PageTemplate {

  private static final Logger log = LogHelper.getLogger();

  protected WebDriver driver;

  public void init(WebDriver driver) {
    log.info("Initializing Page: {}", this.getClass().getName());
    this.driver = driver;
    PageFactory.initElements(driver, this);
  }
}