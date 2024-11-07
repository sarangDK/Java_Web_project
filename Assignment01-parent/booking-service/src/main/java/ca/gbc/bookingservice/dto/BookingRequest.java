package ca.gbc.bookingservice.dto;

import java.util.Date;

public record BookingRequest(
        Long bookingId,
        String bookingNumber,
        Long userId,
        Long roomId,
        Date checkIn,
        Date checkOut,
        String purpose
) {
}
