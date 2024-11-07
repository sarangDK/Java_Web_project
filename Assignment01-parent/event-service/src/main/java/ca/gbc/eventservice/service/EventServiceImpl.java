package ca.gbc.eventservice.service;

import ca.gbc.eventservice.client.UserClient;
import ca.gbc.eventservice.dto.EventRequest;
import ca.gbc.eventservice.dto.EventResponse;
import ca.gbc.eventservice.model.Event;
import ca.gbc.eventservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final MongoTemplate mongoTemplate;
    private final UserClient userClient;

    @Override
    public EventResponse createEvent(EventRequest eventRequest) {

        // get check user role
        var isStaff = userClient.isStaff(eventRequest.userId());

        if (isStaff.equals("Staff")) {
            log.debug("Create event for room: {}", eventRequest.userId());
            Event event = Event.builder()
                    .userId(eventRequest.userId())
                    .eventName(eventRequest.eventName())
                    .eventType(eventRequest.eventType())
                    .expectedAttendees(eventRequest.expectedAttendees())
                    .build();

            eventRepository.save(event);
            log.info("Event {} is saved successfully", event.getEventId());

            return new EventResponse(
                    event.getEventId(),
                    event.getUserId(),
                    event.getEventName(),
                    event.getEventType(),
                    event.getExpectedAttendees()
            );
        } else {
            throw new RuntimeException("User with id " + eventRequest.userId() + " is not staff, can't create an event");
        }
    }

    @Override
    public List<EventResponse> getAllEvents() {
        log.debug("Returning all events");
        List<Event> events = eventRepository.findAll();
        return events.stream().map(this::mapEventToEventResponse).toList();
    }
    private EventResponse mapEventToEventResponse(Event event) {
        return new EventResponse(
                event.getEventId(),
                event.getUserId(),
                event.getEventName(),
                event.getEventType(),
                event.getExpectedAttendees());
    }

    @Override
    public EventResponse getEventById(String eventId) {
        log.debug("Retrieving event by id: {}", eventId);
        Query query = new Query();
        query.addCriteria(Criteria.where("eventId").is(eventId));
        Event event = mongoTemplate.findOne(query, Event.class);

        if(event != null) {
            log.info("Event {} retrieved successfully", eventId);
            return mapEventToEventResponse(event);
        } else {
            log.error("Event {} not found", eventId);
            return null;
        }
    }

    @Override
    public String updateEvent(String eventId, EventRequest eventRequest) {
        log.debug("Revise event for room: {}", eventRequest.userId());
        Query query = new Query();
        query.addCriteria(Criteria.where("eventId").is(eventId));
        Event event = mongoTemplate.findOne(query, Event.class);

        if (event != null) {
            event.setUserId(eventRequest.userId());
            event.setEventName(eventRequest.eventName());
            event.setEventType(eventRequest.eventType());
            event.setExpectedAttendees(eventRequest.expectedAttendees());
            eventRepository.save(event);
            log.info("Event {} is updated successfully", event.getEventId());
            return event.getEventId();
        } else {
            log.error("Event {} is not found", eventId);
            return null;
        }
    }

    @Override
    public void deleteEvent(String eventId) {
        log.debug("Deleting event: {}", eventId);
        Query query = new Query();
        query.addCriteria(Criteria.where("eventId").is(eventId));
        Event event = mongoTemplate.findOne(query, Event.class);

        if (event != null) {
            eventRepository.delete(event);
            log.info("Event {} is  deleted successfully", eventId);
        } else {
            log.error("Event {} is not found", eventId);
        }
    }
}
