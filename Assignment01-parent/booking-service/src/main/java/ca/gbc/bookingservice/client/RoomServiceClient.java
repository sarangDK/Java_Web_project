package ca.gbc.bookingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "room-service", url = "${room.service.url}")
public interface RoomServiceClient {

    @RequestMapping(method = RequestMethod.GET, value="/api/rooms/availability/{id}")
    boolean isRoomAvailable(@PathVariable("id") Long id);

    @RequestMapping(method = RequestMethod.PUT, value="/api/rooms/availability/{id}")
    void updateRoomAvailability(@PathVariable("id") Long id, @RequestParam("availability") boolean availability);
}




