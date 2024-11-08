package ca.gbc.approvalservice.controller;

import ca.gbc.approvalservice.dto.ApprovalRequest;
import ca.gbc.approvalservice.dto.ApprovalResponse;
import ca.gbc.approvalservice.dto.EventResponse;
import ca.gbc.approvalservice.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/approval")
public class ApprovalController {
    private final ApprovalService approvalService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApprovalResponse> createApproval(@RequestBody ApprovalRequest approvalRequest) {
        ApprovalResponse createApproval = approvalService.createApproval(approvalRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/approval/" + createApproval.approvalId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createApproval);
    }

    @GetMapping
    public ResponseEntity<List<ApprovalResponse>> getAllApprovals() {
        List<ApprovalResponse> approvals = approvalService.getAllApproval();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(approvals);
    }

    @PutMapping("/{approvalId}")
    public ResponseEntity<String> updateApproval(@PathVariable String approvalId, @RequestBody ApprovalRequest approvalRequest) {
        String updatedApprovalId = approvalService.updateApproval(approvalId, approvalRequest);

        if (updatedApprovalId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Approval not found");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/approval/" + updatedApprovalId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body("Approval updated successfully");
    }

    @DeleteMapping("/{approvalId}")
    public ResponseEntity<String> deleteApproval(@PathVariable String approvalId) {
        approvalService.deleteApproval(approvalId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Approval deleted successfully");
    }

    @GetMapping("/eventDetails/{eventId}")
    public ResponseEntity<EventResponse> getEventDetails(@PathVariable String eventId) {
        return approvalService.getEventDetails(eventId);
    }

    @GetMapping("/verify/{userId}")
    public String verifyUser(@PathVariable Long userId) {
        return approvalService.verifyUser(userId);
    }
}
