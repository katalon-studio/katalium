package com.katalon.kata.testng;

import com.katalon.kata.helper.GeneratorHelper;
import com.katalon.kata.helper.LogHelper;
import com.katalon.kata.helper.ParameterHelper;
import com.katalon.kata.katalon.*;

import org.slf4j.Logger;
import org.testng.*;
import org.testng.reporters.XMLReporter;
import org.testng.util.Strings;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.katalon.kata.helper.Constants.KATA_PROJECT_ID_PARAMETER;

public class KatalonListener implements ITestListener, IExecutionListener, IReporter {

    private static final Logger log = LogHelper.getLogger();

    private String email;
    private String password;
    private Long projectId;

    private KatalonAnalyticsConnector connector;
    private String token;
    private String sessionId;
    private String suiteName;
    private long executionId;
    private String reportPath;
    private String executionUrl;

    private ParameterHelper parameterHelper = ParameterHelper.getInstance();

    @Override
    public void onExecutionStart() {
        KatalonProperties katalonProperties = new KatalonProperties();
        email = katalonProperties.getEmail();
        password = katalonProperties.getPassword();
        projectId = null;
        connector = new KatalonAnalyticsConnector();
        log.info("Requesting token with user credentials.");
        token = connector.requestToken(email, password);
        sessionId = GeneratorHelper.generateUniqueValue();
    }

    @Override
    public void onStart(ITestContext context) {
        try {
            String project = parameterHelper.getParameterDefaultValue(KATA_PROJECT_ID_PARAMETER);
            if (!Strings.isNullOrEmpty(project)) {
                this.projectId =  Long.valueOf(project);
                Execution execution = connector.createExecution(token, projectId, sessionId);
                executionId = execution.getId();
                executionUrl = execution.getWebUrl();
            } else {
                log.info("For real-time monitoring and better reporting capabilities please integrate this project with Katalon Analytics " +
                  "(more details at https://docs.katalon.com/katalon-analytics/docs/integration-with-katalon-studio.html#enable-integration)");
            }
            XmlTest xmlTest = context.getCurrentXmlTest();
            XmlSuite xmlSuite = xmlTest.getSuite();
            suiteName = xmlSuite.getName();
        } catch (Exception ignored) {
            log.error("Cannot create or get execution (projectId: {}, sessionId: {})", projectId, sessionId);
        }

    }

    @Override
    public void onTestStart(ITestResult result) {
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExecutionStatus status = ExecutionStatus.PASSED;
        updateResult(result, status, false);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExecutionStatus status = ExecutionStatus.FAILED;
        updateResult(result, status, false);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExecutionStatus status = ExecutionStatus.INCOMPLETE;
        updateResult(result, status, false);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ExecutionStatus status = ExecutionStatus.PASSED;
        updateResult(result, status, false);
    }

    @Override
    public void onFinish(ITestContext context) {
    }

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        Path path = Paths.get(outputDirectory, XMLReporter.FILE_NAME);
        reportPath = path.toString();
    }

    @Override
    public void onExecutionFinish() {
        try {
            if (projectId != null) {
                UploadInfo uploadInfo = connector.getUploadInfo(token, projectId);
                String uploadUrl = uploadInfo.getUploadUrl();
                String uploadedPath = uploadInfo.getPath();

                log.info("Uploading report.");
                log.debug("Uploading TestNG report: {}", reportPath);
                File uploadFile = new File(reportPath);
                String batch = System.currentTimeMillis() + "-" + sessionId;
                String folderName = uploadFile.getParent();
                String fileName = uploadFile.getName();

                connector.uploadFile(uploadUrl, uploadFile);
                connector.uploadFileInfo(
                        executionId,
                        projectId,
                        batch,
                        folderName,
                        fileName,
                        uploadedPath,
                        true,
                        token
                );

                log.info("Katalon Analytics - Execution URL: {}", executionUrl);
            }
        } catch (Exception e) {
            log.error("Failed to upload TestNG report to server.");
        }

    }

    private void updateResult(ITestResult result, ExecutionStatus executionStatus, boolean end) {
        if (projectId != null) {
            String name = result.getInstanceName() + "/" + result.getName();

            TestRunResult testRunResult = new TestRunResult();
            testRunResult.setName(name);
            testRunResult.setSessionId(sessionId);
            testRunResult.setTestSuiteId(suiteName);
            testRunResult.setStatus(executionStatus);
            testRunResult.setEnd(end);

            connector.updateExecutionResult(token, projectId, testRunResult);
        }
    }
}
