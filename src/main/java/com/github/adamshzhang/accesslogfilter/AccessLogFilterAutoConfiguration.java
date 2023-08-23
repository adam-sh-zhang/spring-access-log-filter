package com.github.adamshzhang.accesslogfilter;


import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Configuration file to initialize the required beans
 */
@Configuration
@ConditionalOnProperty(
        prefix = "spring.access-log-filter",
        name = {"enabled"},
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties({AccessLogFilterConfigProperties.class})
public class AccessLogFilterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(AccessLogLogger.class)
    public AccessLogLogger accessLogLogger(AccessLogFilterConfigProperties accessLogFilterConfigProperties) {
        return new DefaultAccessLogLogger(accessLogFilterConfigProperties);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnMissingBean(AccessLogFilter.class)
    public AccessLogFilter accessLogFilter(AccessLogLogger accessLogLogger,
                                           AccessLogFilterConfigProperties accessLogFilterConfigProperties) {
        return new AccessLogFilter(accessLogLogger, accessLogFilterConfigProperties);
    }

}
