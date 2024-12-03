package ca.gbc.bookingservice.client;

import groovy.util.logging.Slf4j;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

@Slf4j
public interface RoomServiceClient {

    @GetExchange("/api/room/availability/{id}")
    boolean isRoomAvailable(@PathVariable("id") Long id);

    @PutExchange("/api/v1/room/availability/{id}")
    void updateRoomAvailability(@PathVariable("id") Long id, @RequestParam("availability") boolean availability);
}




