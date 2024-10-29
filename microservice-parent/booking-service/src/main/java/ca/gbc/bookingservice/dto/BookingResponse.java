package ca.gbc.bookingservice.dto;

import java.util.Date;

public record BookingResponse(
        String booking_id,
        String user_id,
        String room_id,
        Date start_time,
        Date end_time,
        String purpose
) {

}
