package ca.gbc.roomservice.service;

import ca.gbc.roomservice.dto.RoomRequest;
import ca.gbc.roomservice.dto.RoomResponse;
import ca.gbc.roomservice.model.Room;
import ca.gbc.roomservice.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;


    @Override
    public void createRoom(RoomRequest roomRequest) {
        Room room = Room.builder()
                .room_id(generateRoomId())
                .room_name(roomRequest.room_name())
                .room_capacity(roomRequest.room_capacity())
                .room_availability(roomRequest.room_availability())
                .room_features(roomRequest.room_features())
                .build();

        roomRepository.save(room);
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(this::mapRoomToRoomResponse)
                .toList();
    }

    private RoomResponse mapRoomToRoomResponse(Room room) {
        return new RoomResponse(
                room.getRoom_id(),
                room.getRoom_name(),
                room.getRoom_capacity(),
                room.getRoom_availability(),
                room.getRoom_features()
        );
    }

    @Override
    public RoomResponse updateRoom(Long id, RoomRequest roomRequest) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setRoom_name(roomRequest.room_name());
        room.setRoom_capacity(roomRequest.room_capacity());
        room.setRoom_availability(roomRequest.room_availability());
        room.setRoom_features(roomRequest.room_features());

        Room updatedRoom = roomRepository.save(room);
        return mapRoomToRoomResponse(updatedRoom);
    }

    @Override
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        roomRepository.delete(room);
    }

    @Override
    public RoomResponse getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        return mapRoomToRoomResponse(room);
    }

    private Long generateRoomId() {
        return roomRepository.count() + 1;
    }
}
