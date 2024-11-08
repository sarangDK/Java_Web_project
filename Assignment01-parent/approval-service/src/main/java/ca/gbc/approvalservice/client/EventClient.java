package ca.gbc.approvalservice.client;


import ca.gbc.approvalservice.dto.EventResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "event-service", url = "${event.service.url}")
public interface EventClient {
    @RequestMapping(method = RequestMethod.GET, value="/api/v1/event/{eventId}")
    ResponseEntity<EventResponse> getEventById(@PathVariable String eventId);
}


