package com.katalon.kata.testng;

import com.katalon.kata.helper.LogHelper;
import com.katalon.kata.webdriver.WebDriverPool;
import org.slf4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

  private static final Logger log = LogHelper.getLogger();

  @Override
  public void onTestStart(ITestResult result) {
  }

  @Override
  public void onTestSuccess(ITestResult result) {

  }

  @Override
  public void onTestFailure(ITestResult result) {

  }

  @Override
  public void onTestSkipped(ITestResult result) {

  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

  }

  @Override
  public void onStart(ITestContext context) {

  }

  @Override
  public void onFinish(ITestContext context) {
    log.info("Clean up WebDrivers.");
    WebDriverPool driverPool = WebDriverPool.get();
    driverPool.quitAll();
  }
}
