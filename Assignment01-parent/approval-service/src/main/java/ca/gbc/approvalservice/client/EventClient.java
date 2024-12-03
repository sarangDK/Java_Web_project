package ca.gbc.approvalservice.client;


import ca.gbc.approvalservice.dto.EventResponse;

import groovy.util.logging.Slf4j;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.service.annotation.GetExchange;


@Slf4j
public interface EventClient {

    Logger log = LoggerFactory.getLogger(EventClient.class);

    @GetExchange("api/v1/event/{eventId}")
    @CircuitBreaker(name = "event", fallbackMethod = "fallbackMethod")
    @RequestMapping(method = RequestMethod.GET, value="/api/v1/event/{eventId}")
    ResponseEntity<EventResponse> getEventById(@PathVariable String eventId);

    default boolean fallbackMethod(String eventName, Throwable throwable) {
        log.info("Cannot get event for event name {}, failure reason: {}", eventName, throwable.getMessage());
        return false;
    }
}


