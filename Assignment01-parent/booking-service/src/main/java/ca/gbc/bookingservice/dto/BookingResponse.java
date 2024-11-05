package ca.gbc.bookingservice.dto;

import java.util.Date;

public record BookingResponse(
        String bookingId,
        String userId,
        Long roomId,
        Date checkIn,
        Date checkOut,
        String purpose
) {


}
