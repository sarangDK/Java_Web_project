package ca.gbc.bookingservice.dto;

import java.util.Date;

public record BookingRequest(
        String booking_id,
        String user_id,
        Long room_id,
        Date start_time,
        Date end_time,
        String purpose
) {
}
