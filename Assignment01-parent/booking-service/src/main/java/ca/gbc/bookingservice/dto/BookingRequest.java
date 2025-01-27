package ca.gbc.bookingservice.dto;

import java.util.Date;

public record BookingRequest(
        String bookingId,
        String bookingNumber,
        Long userId,
        Long roomId,
        Date checkIn,
        Date checkOut,
        String purpose,
        UserDetails userDetails) {
    public record UserDetails(
            String firstName,
            String lastName,
            String email,
            String phoneNumber
    ) {}
}

