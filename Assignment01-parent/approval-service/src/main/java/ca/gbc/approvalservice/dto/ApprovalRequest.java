package ca.gbc.approvalservice.dto;

public record ApprovalRequest(
        String approvalId,
        Long userId,
        String eventId,
        boolean isApproved
) {
}
