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


@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public BookingResponse createBooking(BookingRequest bookingRequest) {

        log.debug("Creating booking for user: {}", bookingRequest.userId());
        Booking booking = Booking.builder()
                .userId(bookingRequest.userId())
                .roomId(bookingRequest.roomId())
                .checkIn(bookingRequest.checkIn())
                .checkOut(bookingRequest.checkOut())
                .purpose(bookingRequest.purpose())
                .build();

        bookingRepository.save(booking);
        log.info("Booking {} is saved successfully", booking.getBookingId());

        return new BookingResponse(
                booking.getBookingId(),
                booking.getUserId(),
                booking.getRoomId(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getPurpose());
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
                booking.getUserId(),
                booking.getRoomId(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getPurpose());
    }

    @Override
    public String updateBooking(String bookingId, BookingRequest bookingRequest) {
        log.debug("Revise booking for user: {}", bookingRequest.userId());
        Query query = new Query();
        query.addCriteria(Criteria.where("bookingId").is(bookingId));
        Booking booking = mongoTemplate.findOne(query, Booking.class);

        if (booking != null) {
            booking.setUserId(bookingRequest.userId());
            booking.setRoomId(bookingRequest.roomId());
            booking.setCheckIn(bookingRequest.checkIn());
            booking.setCheckOut(bookingRequest.checkOut());
            booking.setPurpose(bookingRequest.purpose());
            bookingRepository.save(booking);
            log.info("Booking {} is updated successfully", booking.getBookingId());
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
            log.info("Booking {} is deleted successfully", bookingId);
        } else {
            log.error("Booking {} is not found", bookingId);
        }
    }
}