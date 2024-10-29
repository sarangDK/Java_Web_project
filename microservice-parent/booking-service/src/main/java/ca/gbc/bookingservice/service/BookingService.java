package ca.gbc.bookingservice.service;



import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest bookingRequest);
    List<BookingResponse> getAllBookings();
    String updateProduct(String booking_id, BookingRequest bookingRequest);
    void deleteBooking(String booking_id);
    BookingResponse getBookingById(String booking_id);




}
