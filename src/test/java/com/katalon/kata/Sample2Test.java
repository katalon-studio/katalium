package com.katalon.kata;

import com.katalon.kata.helper.LogHelper;
import com.katalon.kata.testng.TestTemplate;
import org.slf4j.Logger;
import org.testng.annotations.Test;

public class Sample2Test extends TestTemplate {

  private static final Logger log = LogHelper.getLogger();

  @Test
  public void sample2TestStep1() {
    log.info("sample2TestStep1 1");
  }
}
