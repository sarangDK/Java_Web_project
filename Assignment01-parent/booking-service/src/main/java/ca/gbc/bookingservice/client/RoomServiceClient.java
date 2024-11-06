package ca.gbc.bookingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "room-service", url = "${room.service.url}")
public interface RoomServiceClient {

    @GetMapping("/api/rooms/{id}/availability")
    boolean isRoomAvailable(@PathVariable("id") Long roomId);
}



