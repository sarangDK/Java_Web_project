package ca.gbc.approvalservice.service;

import ca.gbc.approvalservice.dto.ApprovalRequest;
import ca.gbc.approvalservice.dto.ApprovalResponse;
import ca.gbc.approvalservice.dto.EventResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ApprovalService {
    ApprovalResponse createApproval(ApprovalRequest approvalRequest);

    List<ApprovalResponse> getAllApproval();

    ApprovalResponse getApprovalById(String approvalId);

    String updateApproval(String approvalId, ApprovalRequest approvalRequest);

    void deleteApproval(String approvalId);

    ResponseEntity<EventResponse> getEventDetails(String eventId);
}
