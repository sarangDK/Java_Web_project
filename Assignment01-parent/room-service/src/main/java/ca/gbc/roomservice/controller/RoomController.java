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
    public RoomResponse createRoom(@RequestBody RoomRequest roomRequest) {
        return roomService.createRoom(roomRequest);
    }


    // Get all rooms
    @GetMapping
    public List<RoomResponse> getAllRooms() {
        return roomService.getAllRooms();
    }

    // Get room by ID operation
    @GetMapping("/{id}")
    public RoomResponse getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }

    // Update room operation
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoomResponse updateRoom(@PathVariable Long id, @RequestBody RoomRequest roomRequest) {
        return roomService.updateRoom(id, roomRequest);
    }
    // Delete room operation
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }

    // Check if a room is available
    @GetMapping("/{id}/availability")
    public boolean isRoomAvailable(@PathVariable Long id) {
        return roomService.isRoomAvailable(id);
    }


}
