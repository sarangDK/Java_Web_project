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
    public boolean isRoomAvailable(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        return room.getRoomAvailability();
    }




    @Override
    public List<RoomResponse> getAvailableRooms() {
        return roomRepository.findAll()
                .stream()
                .filter(Room::getRoomAvailability)
                .map(this::mapRoomToRoomResponse)
                .toList();
    }

    @Override
    public RoomResponse createRoom(RoomRequest roomRequest) {
        Room room = Room.builder()
                .roomId(generateRoomId())
                .roomName(roomRequest.roomName())
                .roomCapacity(roomRequest.roomCapacity())
                .roomAvailability(roomRequest.roomAvailability())
                .roomFeatures(roomRequest.roomFeatures())
                .build();

        roomRepository.save(room);
        return mapRoomToRoomResponse(room);
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
                room.getRoomId(),
                room.getRoomName(),
                room.getRoomCapacity(),
                room.getRoomAvailability(),
                room.getRoomFeatures()
        );
    }

    @Override
    public RoomResponse updateRoom(Long id, RoomRequest roomRequest) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setRoomName(roomRequest.roomName());
        room.setRoomCapacity(roomRequest.roomCapacity());
        room.setRoomAvailability(roomRequest.roomAvailability());
        room.setRoomFeatures(roomRequest.roomFeatures());

        Room updatedRoom = roomRepository.save(room);
        return mapRoomToRoomResponse(updatedRoom);
    }

    @Override
    public void updateRoomAvailability(Long roomId, boolean roomAvailability) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.setRoomAvailability(roomAvailability);
        roomRepository.save(room);
        log.info("Room availability :" + roomAvailability);
    }

    @Override
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        roomRepository.delete(room);
        log.info("Room with id: {} deleted successfully", roomId);
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
