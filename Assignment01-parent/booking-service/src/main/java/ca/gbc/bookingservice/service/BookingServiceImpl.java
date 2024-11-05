package ca.gbc.bookingservice.service;

import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import ca.gbc.bookingservice.model.Booking;
import ca.gbc.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public BookingResponse createBooking(BookingRequest bookingRequest) {
//        Optional<Booking> existingBooking = bookingRepository.findByRoom_id(bookingRequest.room_id());

//        if (existingBooking.isPresent()) {
//            throw new RuntimeException("Room is already booked");
//        }
        log.debug("Creating booking for user: {}", bookingRequest.user_id());
        Booking booking = Booking.builder()
                .user_id(bookingRequest.user_id())
                .room_id(bookingRequest.room_id())
                .start_time(bookingRequest.start_time())
                .end_time(bookingRequest.end_time())
                .purpose(bookingRequest.purpose())
                .build();

        bookingRepository.save(booking);
        log.info("Booking {} is saved successfully", booking.getBooking_id());

        return new BookingResponse(
                booking.getBooking_id(),
                booking.getUser_id(),
                booking.getRoom_id(),
                booking.getStart_time(),
                booking.getEnd_time(),
                booking.getPurpose());
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        log.debug("Returning all bookings");
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream().map(this::mapBookingToBookingResponse).toList();
    }

    @Override
    public BookingResponse getBookingById(String booking_id) {
        log.debug("Retrieving booking by id: {}", booking_id);
        Query query = new Query();
        query.addCriteria(Criteria.where("booking_id").is(booking_id));
        Booking booking = mongoTemplate.findOne(query, Booking.class);

        if (booking != null) {
            log.info("Booking {} retrieved successfully", booking_id);
            return mapBookingToBookingResponse(booking);
        } else {
            log.error("Booking {} not found", booking_id);
            return null;
        }
    }

    private BookingResponse mapBookingToBookingResponse(Booking booking) {
        return new BookingResponse(
                booking.getBooking_id(),
                booking.getUser_id(),
                booking.getRoom_id(),
                booking.getStart_time(),
                booking.getEnd_time(),
                booking.getPurpose());
    }

    @Override
    public String updateBooking(String booking_id, BookingRequest bookingRequest) {
        log.debug("Revise booking for user: {}", bookingRequest.user_id());
        Query query = new Query();
        query.addCriteria(Criteria.where("booking_id").is(booking_id));
        Booking booking = mongoTemplate.findOne(query, Booking.class);

        if (booking != null) {
            booking.setUser_id(bookingRequest.user_id());
            booking.setRoom_id(bookingRequest.room_id());
            booking.setStart_time(bookingRequest.start_time());
            booking.setEnd_time(bookingRequest.end_time());
            booking.setPurpose(bookingRequest.purpose());
            bookingRepository.save(booking);
            log.info("Booking {} is updated successfully", booking.getBooking_id());
            return booking.getBooking_id();
        } else {
            log.error("Booking {} is not found", booking_id);
            return null;
        }
    }

    @Override
    public void deleteBooking(String booking_id) {
        log.debug("Deleting booking: {}", booking_id);
        Query query = new Query();
        query.addCriteria(Criteria.where("booking_id").is(booking_id));
        Booking booking = mongoTemplate.findOne(query, Booking.class);

        if (booking != null) {
            bookingRepository.delete(booking);
            log.info("Booking {} is deleted successfully", booking_id);
        } else {
            log.error("Booking {} is not found", booking_id);
        }
    }
}