# Spring Access Log Filter
A common filter to record access logs(include request & response body) for spring applications

## How to use

##### **Download and install to local maven repository**
```shell
mvn clean install
```

##### **Add maven dependency to your application project**
```xml
    <dependency>
        <groupId>com.github.adamshzhang</groupId>
        <artifactId>spring-access-log-filter</artifactId>
        <version>1.0</version>
    </dependency>
```

##### **Add configs in application.yml**
```yaml
spring:
  access-log-filter:
    # enable filter or not, default is true
    enabled: true
    # auto generate request id or not, default is true
    auto-generate-request-id: true
    #max request body size recorded in log, default is 1024 * 1024
    request-body-max-size: 1024 * 1024
    #max response body size recorded in log, default is 1024 * 1024
    response-body-max-size: 1024 * 1024
    #max response body size recorded in log, default is 1024 * 1024
    ignore-urls:
      - /**
    # dateTimeFormat to format the log request time
    dateTimeFormat: "yyyy-MM-dd HH:mm:ss.SSS"
```

##### View result
Start your spring application, and access any apis exposed by the application. And then you will see a logging like below:
```
2023-08-21 14:34:06.665  INFO [demo] 24572 --- [  XNIO-1 task-1] c.g.a.a.DefaultAccessLogLogger  : {"requestId":null,"requestUrl":"http://localhost/test","requestUri":"/test","requestTime": ....
```

## How to customize

### Customize Logger
The default action to store the access log is simply use application logger to store the log. Where the log is stored is based on your application log setting. If you want to customize the logger action. You can simply create a new Logger class which extend the DefaultAccessLogLogger class or implement the AccessLogLogger interface like below.
```java
import com.github.adamshzhang.accesslogfilter.AccessLog;
import com.github.adamshzhang.accesslogfilter.AccessLogFilterConfigProperties;
import com.github.adamshzhang.accesslogfilter.DefaultAccessLogLogger;

/**
 * Customized AccessLogLogger
 */
public class CustomizedAccessLogLogger extends DefaultAccessLogLogger {
    public CustomizedAccessLogLogger(AccessLogFilterConfigProperties accessLogFilterConfigProperties) {
        super(accessLogFilterConfigProperties);
    }

    @Override
    public void log(AccessLog accessLog) {
        // write your own log logic here
    }
}
```

### Customize Log
We have provided some configs for you to customize your log content. But sometimes you may want to do more. To implement this, you can simply create a new CustomizedAccessLogFilter class which extend the AccessLogFilter to overwrite the methods you want to customize.

Here is an example:
```java

import com.github.adamshzhang.accesslogfilter.AccessLog;
import com.github.adamshzhang.accesslogfilter.AccessLogFilter;
import com.github.adamshzhang.accesslogfilter.AccessLogFilterConfigProperties;
import com.github.adamshzhang.accesslogfilter.AccessLogLogger;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Customized AccessLogFilter
 */
public class CustomizedAccessLogFilter extends AccessLogFilter {
    public CustomizedAccessLogFilter(AccessLogLogger accessLogLogger, AccessLogFilterConfigProperties accessLogFilterConfigProperties) {
        super(accessLogLogger, accessLogFilterConfigProperties);
    }

    /**
     * Overwrite this method, you can fully customize the filter logic
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        super.doFilterInternal(request, response, filterChain);
    }

    /**
     * Overwrite this method, you can fully customize the filter logic
     */
    @Override
    protected AccessLog prepareAccessLogRecord(ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper, Long startAt) {
        AccessLog accessLog = super.prepareAccessLogRecord(requestWrapper, responseWrapper, startAt);
        // customize your own log here
        return accessLog;
    }
}
```
