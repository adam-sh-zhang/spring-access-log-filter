package com.github.adamshzhang.accesslogfilter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.ObjectUtils;

/**
 * Properties class to handle the configs in yaml
 */
@ConfigurationProperties(prefix = "spring.access-log-filter")
public class AccessLogFilterConfigProperties {

    private final Integer DEFAULT_REQUEST_BODY_MAX_SIZE = 1024 * 1024;
    private final Integer DEFAULT_RESPONSE_BODY_MAX_SIZE = 1024 * 1024;
    private final String DEFAULT_ENABLED = "true";
    private final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    private String enabled;

    private Integer requestBodyMaxSize;

    private Integer responseBodyMaxSize;

    private String dateTimeFormat;

    public String[] getIgnoreUrls() {
        return ignoreUrls;
    }

    public void setIgnoreUrls(String[] ignoreUrls) {
        this.ignoreUrls = ignoreUrls;
    }

    private String[] ignoreUrls;

    public String getEnabled() {
        if (ObjectUtils.isEmpty(enabled)) {
            return DEFAULT_ENABLED;
        }
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public Integer getRequestBodyMaxSize() {
        if (requestBodyMaxSize == null) {
            return DEFAULT_REQUEST_BODY_MAX_SIZE;
        }
        return requestBodyMaxSize;
    }

    public void setRequestBodyMaxSize(Integer requestBodyMaxSize) {
        this.requestBodyMaxSize = requestBodyMaxSize;
    }

    public Integer getResponseBodyMaxSize() {
        if (responseBodyMaxSize == null) {
            return DEFAULT_RESPONSE_BODY_MAX_SIZE;
        }
        return responseBodyMaxSize;
    }

    public void setResponseBodyMaxSize(Integer responseBodyMaxSize) {
        this.responseBodyMaxSize = responseBodyMaxSize;
    }

    public String getDateTimeFormat() {
        if (ObjectUtils.isEmpty(dateTimeFormat)) {
            return DEFAULT_DATE_FORMAT;
        }
        return dateTimeFormat;
    }

    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }
}
