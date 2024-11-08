package ca.gbc.bookingservice.repository;

import ca.gbc.bookingservice.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface BookingRepository extends MongoRepository<Booking, String> {
    Booking findByBookingId(String bookingId);

}
