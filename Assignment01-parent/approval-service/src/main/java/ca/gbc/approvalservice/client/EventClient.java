package ca.gbc.approvalservice.client;


import ca.gbc.approvalservice.dto.EventResponse;

import groovy.util.logging.Slf4j;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;


@Slf4j
public interface EventClient {

    Logger log = LoggerFactory.getLogger(EventClient.class);

    @GetExchange("api/v1/event/{eventId}")
    @CircuitBreaker(name = "event", fallbackMethod = "fallbackMethod")
    @Retry(name = "event")
    ResponseEntity<EventResponse> getEventById(@PathVariable String eventId);

    default boolean fallbackMethod(String eventName, Throwable throwable) {
        log.info("Cannot get event for event name {}, failure reason: {}", eventName, throwable.getMessage());
        return false;
    }
}


