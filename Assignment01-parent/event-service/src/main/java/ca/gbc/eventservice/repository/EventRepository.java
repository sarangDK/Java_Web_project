package ca.gbc.eventservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ca.gbc.eventservice.model.Event;

public interface EventRepository extends MongoRepository<Event, String> {
}
