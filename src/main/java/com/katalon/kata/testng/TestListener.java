package com.katalon.kata.testng;

import com.katalon.kata.helper.LogHelper;
import com.katalon.kata.webdriver.WebDriverPool;
import org.slf4j.Logger;
import org.testng.IExecutionListener;

public class TestListener implements IExecutionListener {

  private static final Logger log = LogHelper.getLogger();

  @Override
  public void onExecutionStart() {

  }

  @Override
  public void onExecutionFinish() {
    log.info("Clean up WebDrivers.");
    WebDriverPool driverPool = WebDriverPool.get();
    driverPool.quitAll();
  }
}
