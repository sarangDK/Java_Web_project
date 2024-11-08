package ca.gbc.bookingservice.controller;


import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import ca.gbc.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor


public class BookingController {

    private final BookingService bookingService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest bookingRequest) {
        BookingResponse createdBooking = bookingService.createBooking(bookingRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/booking/" + createdBooking.bookingId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdBooking);


    }
    // Get all Bookings
    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookingResponse> bookings = bookingService.getAllBookings();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(bookings);
    }

    // Get booking by ID
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable String bookingId) {
        BookingResponse bookingResponse = bookingService.getBookingById(bookingId);
        if (bookingResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(bookingResponse);
    }


    // Update a booking
    @PutMapping("/{bookingId}")
    public ResponseEntity<String> updateBooking(@PathVariable String bookingId, @RequestBody BookingRequest bookingRequest) {
        String updatedBookingId = bookingService.updateBooking(bookingId, bookingRequest);

        if (updatedBookingId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Booking id: " + bookingId + " not found");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/booking/" + updatedBookingId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body("Booking ID: " + updatedBookingId + " updated successfully");
    }

    // Delete a booking
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<String> deleteBooking(@PathVariable String bookingId) {
        bookingService.deleteBooking(bookingId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Booking Number: " + bookingId + " deleted successfully");
    }

}
