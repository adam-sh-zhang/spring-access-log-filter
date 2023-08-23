package com.github.adamshzhang.accesslogfilter;

import java.util.Date;
import java.util.Map;

/**
 * Access Log Data Model
 */
public class AccessLog {

    /**
     * a unique id for each request
     */
    private String requestId;

    /**
     * request url
     */
    private String requestUrl;

    /**
     * request uri
     */
    private String requestUri;

    /**
     * request start time (server time)
     */
    private Date requestTime;

    /**
     * request duration time
     */
    private Long requestDuration;

    /**
     * request method
     */
    private String methodName;

    /**
     * request params in url
     */
    private String queryString;

    /**
     * remote addr
     */
    private String remoteAddr;

    /**
     * remote host
     */
    private String remoteHost;

    /**
     * username from principal
     */
    private String remoteUser;

    /**
     * remote port
     */
    private Integer remotePort;

    /**
     * all request headers
     */
    private Map<String, String> requestHeaders;

    /**
     * request body
     */
    private String requestBody;

    /**
     * response http status
     */
    private Integer responseStatus;

    /**
     * response body
     */
    private String responseBody;


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }
    
    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Long getRequestDuration() {
        return requestDuration;
    }

    public void setRequestDuration(Long requestDuration) {
        this.requestDuration = requestDuration;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public String getRemoteUser() {
        return remoteUser;
    }

    public void setRemoteUser(String remoteUser) {
        this.remoteUser = remoteUser;
    }

    public Integer getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(Integer remotePort) {
        this.remotePort = remotePort;
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(Map<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}
