package ca.gbc.approvalservice.service;

import ca.gbc.approvalservice.dto.ApprovalRequest;
import ca.gbc.approvalservice.dto.ApprovalResponse;

import java.util.List;

public interface ApprovalService {
    ApprovalResponse createApproval(ApprovalRequest approvalRequest);

    List<ApprovalResponse> getAllApproval();

    ApprovalResponse getApprovalById(String approvalId);

    String updateApproval(String approvalId, ApprovalRequest approvalRequest);

    void deleteApproval(String approvalId);

    void getEventDetails(String eventId);
}
