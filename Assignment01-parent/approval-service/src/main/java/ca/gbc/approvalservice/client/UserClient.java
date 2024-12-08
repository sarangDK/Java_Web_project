package ca.gbc.approvalservice.client;

import groovy.util.logging.Slf4j;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

@Slf4j
public interface UserClient {

    Logger log = LoggerFactory.getLogger(UserClient.class);
    @GetExchange("/api/v1/user/isStaff/{userId}")
    @CircuitBreaker(name = "user", fallbackMethod = "fallbackMethod")
    @Retry(name = "user")
    String isStaff(@PathVariable("userId") Long userId);

    default String fallbackMethod(Long userId, Throwable throwable) {
        log.info("Fallback - Cannot get user for id {}, failure reason: {}", userId, throwable.getMessage());
        return "false";
    }
}
