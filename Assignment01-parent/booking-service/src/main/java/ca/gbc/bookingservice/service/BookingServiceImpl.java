package ca.gbc.bookingservice.service;

import ca.gbc.bookingservice.client.RoomServiceClient;
import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import ca.gbc.bookingservice.event.BookingCreatedEvent;
import ca.gbc.bookingservice.model.Booking;
import ca.gbc.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final MongoTemplate mongoTemplate;
    private final RoomServiceClient roomServiceClient;
    private final KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate;


    @Override
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        try {
            var isRoomAvailable = roomServiceClient.isRoomAvailable(bookingRequest.roomId());

            if (!isRoomAvailable) {
                throw new RuntimeException("Room with roomId: " + bookingRequest.roomId() + " is not available");
            }

            Booking booking = Booking.builder()
                    .bookingNumber(UUID.randomUUID().toString())
                    .userId(bookingRequest.userId())
                    .roomId(bookingRequest.roomId())
                    .checkIn(bookingRequest.checkIn())
                    .checkOut(bookingRequest.checkOut())
                    .purpose(bookingRequest.purpose())
                    .build();

            bookingRepository.save(booking);

            BookingCreatedEvent bookingCreatedEvent = new BookingCreatedEvent(booking.getBookingNumber(), bookingRequest.userDetails().email());
            log.info("Start - Sending OrderPlacedEvent {} to Kafka topic order -- placed", bookingCreatedEvent);
            kafkaTemplate.send("order", "placed", bookingCreatedEvent);
            log.info("Complete - Sent OrderPlacedEvent {} to Kafka topic order -- placed", bookingCreatedEvent);

            roomServiceClient.updateRoomAvailability(bookingRequest.roomId(), false);

            return new BookingResponse(
                    booking.getBookingId(),
                    booking.getBookingNumber(),
                    booking.getUserId(),
                    booking.getRoomId(),
                    booking.getCheckIn(),
                    booking.getCheckOut(),
                    booking.getPurpose()
            );
        } catch (Exception e) {
            log.error("Error occurred while creating booking: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create booking: " + e.getMessage());
        }
    }



    @Override
    public List<BookingResponse> getAllBookings() {
        log.debug("Returning all bookings");
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream().map(this::mapBookingToBookingResponse).toList();
    }

    @Override
    public BookingResponse getBookingById(String bookingId) {
        log.debug("Retrieving booking by id: {}", bookingId);
        Query query = new Query();
        query.addCriteria(Criteria.where("bookingId").is(bookingId));
        Booking booking = mongoTemplate.findOne(query, Booking.class);

        if (booking != null) {
            log.info("Booking {} retrieved successfully", bookingId);
            return mapBookingToBookingResponse(booking);
        } else {
            log.error("Booking {} not found", bookingId);
            return null;
        }
    }


    private BookingResponse mapBookingToBookingResponse(Booking booking) {
        return new BookingResponse(
                booking.getBookingId(),
                booking.getBookingNumber(),
                booking.getUserId(),
                booking.getRoomId(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getPurpose());
    }

    @Override
    public String updateBooking(String bookingId, BookingRequest bookingRequest) {
        log.debug("Update booking for id: {}", bookingId);
        Booking booking = bookingRepository.findByBookingId(bookingId);
        if (booking != null) {
            booking.setUserId(bookingRequest.userId());
            booking.setRoomId(bookingRequest.roomId());
            booking.setBookingNumber(bookingRequest.bookingNumber());
            booking.setCheckIn(bookingRequest.checkIn());
            booking.setCheckOut(bookingRequest.checkOut());
            booking.setPurpose(bookingRequest.purpose());
            bookingRepository.save(booking);
            log.info("Booking id: {} is updated successfully", booking.getBookingId());
            return booking.getBookingId();
        } else {
            log.error("Booking {} is not found", bookingId);
            return null;
        }
    }


    @Override
    public void deleteBooking(String bookingId) {
        log.debug("Deleting booking: {}", bookingId);
        Query query = new Query();
        query.addCriteria(Criteria.where("bookingId").is(bookingId));
        Booking booking = mongoTemplate.findOne(query, Booking.class);

        if (booking != null) {
            bookingRepository.delete(booking);
            log.info("Booking id: {} is deleted successfully", booking.getBookingId());
        } else {
            log.error("Booking id: {} is not found", bookingId);
        }
    }
}