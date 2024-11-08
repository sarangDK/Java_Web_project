package ca.gbc.userservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="t_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    private String user_name;
    private String user_email;

    @ManyToOne
    @JoinColumn(name="type_id", nullable = false)
    private Type type;
}
