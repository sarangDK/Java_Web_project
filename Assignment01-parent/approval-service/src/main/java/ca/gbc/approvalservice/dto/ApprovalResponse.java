package ca.gbc.approvalservice.dto;

public record ApprovalResponse(
        String approvalId,
        Long userId,
        String eventId,
        boolean isApproved
) {
}
