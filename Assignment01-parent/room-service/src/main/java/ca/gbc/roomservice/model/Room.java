package ca.gbc.roomservice.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_rooms")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long room_id;
    private String room_name;
    private String room_capacity;
    private Boolean room_availability;
    private String room_features;
}
