package ca.gbc.roomservice.service;

import ca.gbc.roomservice.dto.RoomRequest;
import ca.gbc.roomservice.dto.RoomResponse;

import java.util.List;

public interface RoomService {
    void createRoom(RoomRequest roomRequest);
    List<RoomResponse> getAllRooms();
    RoomResponse getRoomById(Long roomId);
    RoomResponse updateRoom(Long id, RoomRequest roomRequest);
    void deleteRoom(Long roomId);
}
