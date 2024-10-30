package ca.gbc.roomservice.controller;

import ca.gbc.roomservice.dto.RoomRequest;
import ca.gbc.roomservice.dto.RoomResponse;
import ca.gbc.roomservice.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    // Create a new room
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createRoom(@RequestBody RoomRequest roomRequest) {
        roomService.createRoom(roomRequest);
        return "Room created successfully";
    }

    // Get all rooms
    @GetMapping
    public List<RoomResponse> getAllRooms() {
        return roomService.getAllRooms();
    }

    // Get room by ID
    @GetMapping("/{id}")
    public RoomResponse getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }

    // Update room
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String updateRoom(@PathVariable Long id, @RequestBody RoomRequest roomRequest) {
        roomService.updateRoom(id, roomRequest);
        return "Room updated successfully";
    }

    // Delete room
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return "Room deleted successfully";
    }
}
