package com.katalon.kata.katalon;

import java.util.Date;

public class Execution {
    private Long id;

    private ExecutionStatus status;
    private Date startTime;
    private Date endTime;
    private Date duration;
    private Long totalTests;

    private Long totalPassedTests;
    private Long totalFailedTests;
    private Long totalErrorTests;
    private Long totalIncompleteTests;

    private Long order;
    private Long projectId;
    private String sessionId;
    private String webUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getDuration() {
        return duration;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    public Long getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(Long totalTests) {
        this.totalTests = totalTests;
    }

    public Long getTotalPassedTests() {
        return totalPassedTests;
    }

    public void setTotalPassedTests(Long totalPassedTests) {
        this.totalPassedTests = totalPassedTests;
    }

    public Long getTotalFailedTests() {
        return totalFailedTests;
    }

    public void setTotalFailedTests(Long totalFailedTests) {
        this.totalFailedTests = totalFailedTests;
    }

    public Long getTotalErrorTests() {
        return totalErrorTests;
    }

    public void setTotalErrorTests(Long totalErrorTests) {
        this.totalErrorTests = totalErrorTests;
    }

    public Long getTotalIncompleteTests() {
        return totalIncompleteTests;
    }

    public void setTotalIncompleteTests(Long totalIncompleteTests) {
        this.totalIncompleteTests = totalIncompleteTests;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
