package ca.gbc.eventservice.controller;

import ca.gbc.eventservice.dto.EventRequest;
import ca.gbc.eventservice.dto.EventResponse;
import ca.gbc.eventservice.model.Event;
import ca.gbc.eventservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
public class EventController {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EventResponse> createEvent(@RequestBody EventRequest eventRequest) {
        EventResponse createEvent = eventService.createEvent(eventRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/event/" + createEvent.eventId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createEvent);
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<EventResponse> events = eventService.getAllEvents();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(events);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<String> updateEvent(@PathVariable String eventId, @RequestBody EventRequest eventRequest) {
        String updatedEventId = eventService.updateEvent(eventId, eventRequest);

        if (updatedEventId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Event not found");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/event/" + updatedEventId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body("Event updated successfully");
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable String eventId) {
        eventService.deleteEvent(eventId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Event deleted successfully");
    }
}
