package com.katalon.kata;

import com.katalon.kata.helper.LogHelper;
import com.katalon.kata.testng.TestTemplate;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SampleTest extends TestTemplate {

    private static final Logger log = LogHelper.getLogger();

    @Test
    public void test1() {
        log.info("test 1");
    }

    @Test
    public void test2Failed() {
        System.out.println("This method to test fail");
        Assert.assertTrue(false);
    }
}
