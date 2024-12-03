package ca.gbc.approvalservice.client;

import groovy.util.logging.Slf4j;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.service.annotation.GetExchange;



@Slf4j
public interface UserClient {

    Logger log = LoggerFactory.getLogger(UserClient.class);
    @GetExchange("/api/v1/user")
    @CircuitBreaker(name = "user", fallbackMethod = "fallbackMethod")
    @RequestMapping(method = RequestMethod.GET, value="/api/v1/user/isStaff/{userId}")
    String isStaff(@PathVariable("userId") Long userId);

    default boolean fallbackMethod(String userName, Throwable throwable) {
        log.info("Cannot get user for user name {}, failure reason: {}", userName, throwable.getMessage());
        return false;
    }
}
