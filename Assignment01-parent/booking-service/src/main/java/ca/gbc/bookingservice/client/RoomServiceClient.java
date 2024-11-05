package ca.gbc.bookingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "room-service", url = "${room.service.url}")
public interface RoomServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/rooms/{id}")
    boolean isRoomAvailable(@RequestParam Long room_id, @RequestParam String start_time, @RequestParam String end_time);
}





