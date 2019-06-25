package com.katalon.kata.katalon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.katalon.kata.helper.HttpHelper;
import com.katalon.kata.helper.JsonHelper;
import com.katalon.kata.helper.LogHelper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class KatalonAnalyticsConnector {

    private static final Logger log = LogHelper.getLogger();

    private static final String TESTNG_TEST_REPORTS_URI = "/api/v1/testng/test-reports";

    private static final String UPLOAD_TEST_REPORTS_URI = "/api/v1/katalon/test-reports/update-result";

    private static final String UPLOAD_URL_URI = "/api/v1/files/upload-url";

    private static final String TOKEN_URI = "/oauth/token";

    private ObjectMapper objectMapper;

    private String serverApiUrl;

    public KatalonAnalyticsConnector() {
        KatalonProperties katalonProperties = new KatalonProperties();
        serverApiUrl = katalonProperties.getServerApiUrl();

        objectMapper = JsonHelper.objectMapper();
    }

    public Execution createExecution(String token, long projectId, String sessionId) {
        TestRunResult testRunResult = new TestRunResult();
        testRunResult.setSessionId(sessionId);
        return updateExecutionResult(token, projectId, testRunResult);
    }

    public Execution updateExecutionResult(String token, long projectId, TestRunResult testRunResult) {
        String url = serverApiUrl + UPLOAD_TEST_REPORTS_URI;

        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            uriBuilder.setParameter("projectId", String.valueOf(projectId));

            HttpPost httpPost = new HttpPost(uriBuilder.build());
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            String requestContent = objectMapper.writeValueAsString(testRunResult);
            HttpResponse httpResponse = HttpHelper.sendRequest(
                    httpPost,
                    token,
                    null,
                    null,
                    IOUtils.toInputStream(requestContent, StandardCharsets.UTF_8),
                    null,
                    null);

            InputStream responseContent = httpResponse.getEntity().getContent();
            Execution execution = objectMapper.readValue(responseContent, Execution.class);
            return execution;
        } catch (Exception e) {
            log.error("Cannot send data to server: {}", url, e);
            return null;
        }
    }

    public void uploadFileInfo(
            long executionId,
            long projectId,
            String batch,
            String folderName,
            String fileName,
            String uploadedPath,
            boolean isEnd,
            String token) throws IOException, URISyntaxException {
        String url = serverApiUrl + TESTNG_TEST_REPORTS_URI;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            uriBuilder.setParameter("executionId", String.valueOf(executionId));
            uriBuilder.setParameter("projectId", String.valueOf(projectId));
            uriBuilder.setParameter("batch", batch);
            uriBuilder.setParameter("folderPath", folderName);
            uriBuilder.setParameter("isEnd", String.valueOf(isEnd));
            uriBuilder.setParameter("fileName", fileName);
            uriBuilder.setParameter("uploadedPath", uploadedPath);

            HttpPost httpPost = new HttpPost(uriBuilder.build());
            HttpHelper.sendRequest(
                    httpPost,
                    token,
                    null,
                    null,
                    null,
                    null,
                    null);
        } catch (Exception e) {
            log.error("Cannot send data to server: {}", url, e);
            throw e;
        }
    }

    public UploadInfo getUploadInfo(String token, long projectId) throws Exception {
        String url = serverApiUrl + UPLOAD_URL_URI;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            uriBuilder.setParameter("projectId", String.valueOf(projectId));

            HttpGet httpGet = new HttpGet(uriBuilder.build());

            HttpResponse httpResponse = HttpHelper.sendRequest(
                    httpGet,
                    token,
                    null,
                    null,
                    null,
                    null,
                    null);

            InputStream content = httpResponse.getEntity().getContent();
            UploadInfo uploadInfo = objectMapper.readValue(content, UploadInfo.class);
            return uploadInfo;
        } catch (Exception e) {
            log.error("Cannot send data to server: {}", url, e);
            throw e;
        }
    }

    public void uploadFile(String url, File file) {
        try (InputStream content = new FileInputStream(file)) {
            HttpPut httpPut = new HttpPut(url);
            HttpResponse httpResponse = HttpHelper.sendRequest(
                    httpPut,
                    null,
                    null,
                    null,
                    content,
                    file.length(),
                    null);
            log.info(httpResponse.getStatusLine().getStatusCode() + " " + url);
        } catch (Exception e) {
            log.error("Cannot send data to server: {}", url, e);
        }
    }

    public String requestToken(String email, String password) {
        try {

            String clientId = "kit";
            String clientSecret = "kit";

            String url = serverApiUrl + TOKEN_URI;
            URIBuilder uriBuilder = new URIBuilder(url);

            List<NameValuePair> pairs = Arrays.asList(
                    new BasicNameValuePair("username", email),
                    new BasicNameValuePair("password", password),
                    new BasicNameValuePair("grant_type", "password")
            );

            HttpPost httpPost = new HttpPost(uriBuilder.build());

            String clientCredentials = clientId + ":" + clientSecret;
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " +
                    Base64.getEncoder().encodeToString(clientCredentials.getBytes()));

            HttpResponse httpResponse = HttpHelper.sendRequest(
                    httpPost,
                    null,
                    clientId,
                    clientSecret,
                    null,
                    null,
                    pairs);

            InputStream content = httpResponse.getEntity().getContent();
            Map<String, Object> map = objectMapper.readValue(content, Map.class);
            return (String) map.get("access_token");

        } catch (Exception e) {
            log.error("Cannot get access_token from server by your credentials", e);
            return null;
        }
    }

}
