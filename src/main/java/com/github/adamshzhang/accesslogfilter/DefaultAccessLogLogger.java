package com.github.adamshzhang.accesslogfilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;

/**
 * Default implementation for AccessLogLogger
 * Simply log accessLog object by using system logger
 */
public class DefaultAccessLogLogger implements AccessLogLogger {

    private static final Log logger = LogFactory.getLog(DefaultAccessLogLogger.class);

    private ObjectMapper objectMapper;

    private AccessLogFilterConfigProperties accessLogFilterConfigProperties;

    public DefaultAccessLogLogger(AccessLogFilterConfigProperties accessLogFilterConfigProperties) {
        this.accessLogFilterConfigProperties = accessLogFilterConfigProperties;
        this.objectMapper = new ObjectMapper();
        this.configObjectMapper(objectMapper);
    }

    /**
     * config json objectMapper
     * @param objectMapper
     */
    protected void configObjectMapper(ObjectMapper objectMapper) {
        String dateTimeFormat = accessLogFilterConfigProperties.getDateTimeFormat();
        objectMapper.setDateFormat(new SimpleDateFormat(dateTimeFormat));
    }

    /**
     * method to log accessLog record
     * @param accessLog
     */
    public void log(AccessLog accessLog) {
        try {
            String jsonStr = objectMapper.writeValueAsString(accessLog);
            logger.info(jsonStr);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
