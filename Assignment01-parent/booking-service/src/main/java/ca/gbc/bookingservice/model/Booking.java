package ca.gbc.bookingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(value = "booking")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {
    @Id
    private String bookingId;

    private String bookingNumber;
    private Long userId;
    private Long roomId;
    private Date checkIn;
    private Date checkOut;
    private String purpose;

}
