package com.katalon.kata.testng;

public class TestConfiguration {
    private String sessionId;

    private Boolean disableScreenshot;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Boolean getDisableScreenshot() {
        return disableScreenshot;
    }

    public void setDisableScreenshot(Boolean disableScreenshot) {
        this.disableScreenshot = disableScreenshot;
    }
}
