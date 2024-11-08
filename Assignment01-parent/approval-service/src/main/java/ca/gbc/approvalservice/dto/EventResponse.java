package ca.gbc.approvalservice.dto;

public record EventResponse(
        String eventId,
        Long userId,
        String eventName,
        String eventType,
        Integer expectedAttendees
) {
}
