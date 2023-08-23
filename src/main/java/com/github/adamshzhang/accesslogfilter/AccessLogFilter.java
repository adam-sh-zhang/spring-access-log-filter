package com.github.adamshzhang.accesslogfilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * Core request filter to generate access log
 */
public class AccessLogFilter extends OncePerRequestFilter {

    private AccessLogLogger accessLogLogger;

    private AccessLogFilterConfigProperties accessLogFilterConfigProperties;

    private static final Log logger = LogFactory.getLog(AccessLogFilter.class);

    private AntPathMatcher defaultPathMatcher = new AntPathMatcher();

    public AccessLogFilter(AccessLogLogger accessLogLogger, AccessLogFilterConfigProperties accessLogFilterConfigProperties) {
        this.accessLogLogger = accessLogLogger;
        this.accessLogFilterConfigProperties = accessLogFilterConfigProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.debug("access log filter fired");

        // check need apply filter or not
        boolean needApplyFilter = true;

        // if enabled not equal "true"
        if (!ObjectUtils.nullSafeEquals("true", accessLogFilterConfigProperties.getEnabled())) {
            needApplyFilter = false;
        }

        // if url is ignored by the setting "ignore-urls"
        if (isUrlIgnored(request)) {
            needApplyFilter = false;
        }

        if (!needApplyFilter) {
            // keep original logic
            filterChain.doFilter(request, response);
            return;
        }

        long startAt = System.currentTimeMillis();

        // use wrapper to get request and response body
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request, accessLogFilterConfigProperties.getRequestBodyMaxSize());
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(requestWrapper, responseWrapper);

        try {
            AccessLog accessLog = prepareAccessLogRecord(requestWrapper, responseWrapper, startAt);
            responseWrapper.copyBodyToResponse();
            accessLogLogger.log(accessLog);
        } catch (Exception e) {
            logger.error("process access log failed", e);
        }
        logger.debug("access log filter finished");
    }


    /**
     * create accessLog object and set values from request and response
     *
     * @param requestWrapper
     * @param responseWrapper
     * @param startAt
     * @return
     */
    protected AccessLog prepareAccessLogRecord(ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper, Long startAt) {
        long endAt = System.currentTimeMillis();
        long duration = endAt - startAt;

        AccessLog accessLog = new AccessLog();

        // basic request info
        accessLog.setRequestTime(new Date(startAt));
        accessLog.setRequestDuration(duration);
        accessLog.setRequestId(nullSafeToString(requestWrapper.getAttribute("request-id")));
        accessLog.setMethodName(requestWrapper.getMethod());
        accessLog.setQueryString(requestWrapper.getQueryString());
        accessLog.setRemoteAddr(requestWrapper.getRemoteAddr());
        accessLog.setRemoteHost(requestWrapper.getRemoteHost());
        accessLog.setRemoteUser(requestWrapper.getRemoteUser());
        accessLog.setRemotePort(requestWrapper.getRemotePort());
        accessLog.setRequestUrl(nullSafeToString(requestWrapper.getRequestURL()));
        accessLog.setRequestUri(nullSafeToString(requestWrapper.getRequestURI()));
        Enumeration<String> requestHeaderNames = requestWrapper.getHeaderNames();

        // request headers
        HashMap<String, String> headers = new HashMap<>();
        while (requestHeaderNames.hasMoreElements()) {
            String headerName = requestHeaderNames.nextElement();
            headers.put(headerName, requestWrapper.getHeader(headerName));
        }
        accessLog.setRequestHeaders(headers);

        // request body
        if (this.accessLogFilterConfigProperties.getRequestBodyMaxSize() > 0) {
            try {
                accessLog.setRequestBody(new String(requestWrapper.getContentAsByteArray(), requestWrapper.getCharacterEncoding()));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        // response status
        accessLog.setResponseStatus(responseWrapper.getStatus());

        // response body
        if (this.accessLogFilterConfigProperties.getResponseBodyMaxSize() > 0) {
            int contentLength = responseWrapper.getContentSize();
            if (this.accessLogFilterConfigProperties.getResponseBodyMaxSize() < contentLength) {
                contentLength = this.accessLogFilterConfigProperties.getResponseBodyMaxSize();
            }
            byte[] content = new byte[contentLength];
            try {
                responseWrapper.getContentInputStream().read(content);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                accessLog.setResponseBody(new String(content, responseWrapper.getCharacterEncoding()));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        return accessLog;
    }

    /**
     * check the request is ignored or not
     * @param request
     * @return
     */
    protected boolean isUrlIgnored(HttpServletRequest request) {
        String[] ignoreUrls = accessLogFilterConfigProperties.getIgnoreUrls();
        String uri = nullSafeToString(request.getRequestURI());
        if (!ObjectUtils.isEmpty(ignoreUrls)
                && !ObjectUtils.isEmpty(uri)) {
            boolean ignored = Arrays.stream(ignoreUrls).anyMatch(ignoreUrl -> defaultPathMatcher.match(ignoreUrl, uri));
            if (ignored) {
                logger.info(uri + " has been ignored");
                return true;
            }
        }
        return false;
    }

    /**
     * rewrite the nullSafeToString method in ObjectUtils
     *
     * @param object
     * @return
     */
    private String nullSafeToString(Object object) {
        if (object == null) {
            return null;
        }
        return ObjectUtils.nullSafeToString(object);
    }

}
