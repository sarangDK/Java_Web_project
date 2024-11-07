package ca.gbc.userservice.repository;

import ca.gbc.userservice.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRepository extends JpaRepository<Type, Long> {
}
