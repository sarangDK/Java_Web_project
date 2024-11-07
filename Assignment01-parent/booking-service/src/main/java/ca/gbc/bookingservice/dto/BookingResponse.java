package ca.gbc.bookingservice.dto;

import java.util.Date;

public record BookingResponse(
        Long bookingId,
        String bookingNumber,
        Long userId,
        Long roomId,
        Date checkIn,
        Date checkOut,
        String purpose
) {


}
