package ca.gbc.bookingservice.client;

import groovy.util.logging.Slf4j;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PutExchange;

@Slf4j
public interface RoomServiceClient {

    Logger log = LoggerFactory.getLogger(RoomServiceClient.class);


    @GetExchange("/api/v1/room/availability/{id}")
    @CircuitBreaker(name = "room", fallbackMethod = "fallbackMethod")
    @Retry(name = "room")
    boolean isRoomAvailable(@PathVariable("id") Long id);

    @PutExchange("/api/v1/room/availability/{id}")
    void updateRoomAvailability(@PathVariable("id") Long id, @RequestParam("availability") boolean availability);
    default boolean fallbackMethod(Long id, Throwable throwable) {
        log.info("Cannot get room for id {}, failure reason: {}", id, throwable.getMessage());
        return false;
    }
}




