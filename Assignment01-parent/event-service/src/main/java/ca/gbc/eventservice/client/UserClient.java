package ca.gbc.eventservice.client;

import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

@Slf4j
public interface UserClient {

    Logger log = LoggerFactory.getLogger(UserClient.class);
    @GetExchange("/api/v1/user")
    String isStaff(@PathVariable("userId") Long userId);

    default boolean fallbackMethod(String userName, Throwable throwable) {
        log.info("Cannot get user for user name {}, failure reason: {}", userName, throwable.getMessage());
        return false;
    }
}
