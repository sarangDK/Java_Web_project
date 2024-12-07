package ca.gbc.approvalservice.service;

import ca.gbc.approvalservice.client.EventClient;
import ca.gbc.approvalservice.client.UserClient;
import ca.gbc.approvalservice.dto.ApprovalRequest;
import ca.gbc.approvalservice.dto.ApprovalResponse;
import ca.gbc.approvalservice.dto.EventResponse;
import ca.gbc.approvalservice.model.Approval;
import ca.gbc.approvalservice.repository.ApprovalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ApprovalServiceImpl implements ApprovalService{

    private final ApprovalRepository approvalRepository;
    private final MongoTemplate mongoTemplate;
    private final UserClient userClient;
    private final EventClient eventClient;
    @Override
    public ApprovalResponse createApproval(ApprovalRequest approvalRequest) {

        // get check user role
        var isStaff = userClient.isStaff(approvalRequest.userId());
        //var isStaff = "Staff";
        // event details
        // var eventDetails = eventClient.getEventById(approvalRequest.eventId());
        var eventDetails = 1;
        if (isStaff.equals("Staff")) {
            log.debug("Create approval for userId: {}", approvalRequest.userId());
            Approval approval = Approval.builder()
                    .userId(approvalRequest.userId())
                    .eventId(approvalRequest.eventId())
                    .isApproved(approvalRequest.isApproved())
                    .build();

            approvalRepository.save(approval);
            log.info("Approval {} is saved successfully", approval.getApprovalId());

            return new ApprovalResponse(
                    approval.getApprovalId(),
                    approval.getUserId(),
                    approval.getEventId(),
                    approval.isApproved()
            );
        } else {
            throw new RuntimeException("User with id " + approvalRequest.userId() + " is not staff, can't approve the event");
        }
    }

    @Override
    public List<ApprovalResponse> getAllApproval() {
        log.debug("Returning all approvals");
        List<Approval> approvals = approvalRepository.findAll();
        return approvals.stream().map(this::mapEventToEventResponse).toList();
    }
    private ApprovalResponse mapEventToEventResponse(Approval approval) {
        return new ApprovalResponse(
                approval.getApprovalId(),
                approval.getUserId(),
                approval.getEventId(),
                approval.isApproved()
        );
    }

    @Override
    public ApprovalResponse getApprovalById(String approvalId) {
        log.debug("Retrieving approval by id: {}", approvalId);
        Query query = new Query();
        query.addCriteria(Criteria.where("approvalId").is(approvalId));
        Approval approval = mongoTemplate.findOne(query, Approval.class);

        if(approval != null) {
            log.info("approval {} retrieved successfully", approvalId);
            return mapEventToEventResponse(approval);
        } else {
            log.error("approval {} not found", approvalId);
            return null;
        }
    }

    @Override
    public String updateApproval(String approvalId, ApprovalRequest approvalRequest) {
        log.debug("Revise approval for user: {}", approvalRequest.userId());
        Query query = new Query();
        query.addCriteria(Criteria.where("eventId").is(approvalId));
        Approval approval = mongoTemplate.findOne(query, Approval.class);

        if (approval != null) {
            approval.setApprovalId(approvalRequest.approvalId());
            approval.setUserId(approvalRequest.userId());
            approval.setEventId(approvalRequest.eventId());
            approval.setApproved(approvalRequest.isApproved());
            approvalRepository.save(approval);
            log.info("Approval {} is updated successfully", approval.getApprovalId());
            return approval.getApprovalId();
        } else {
            log.error("Aaproval {} is not found", approvalId);
            return null;
        }
    }

    @Override
    public void deleteApproval(String approvalId) {
        log.debug("Deleting approval: {}", approvalId);
        Query query = new Query();
        query.addCriteria(Criteria.where("eventId").is(approvalId));
        Approval approval = mongoTemplate.findOne(query, Approval.class);

        if (approval != null) {
            approvalRepository.delete(approval);
            log.info("Approval {} is  deleted successfully", approvalId);
        } else {
            log.error("Approval {} is not found", approvalId);
        }
    }

    @Override
    public ResponseEntity<EventResponse> getEventDetails(String eventId) {
        log.info("Fetch the Event with ID: {}", eventId);
        return eventClient.getEventById(eventId);
    }

    @Override
    public String verifyUser(Long userId) {
        log.info("Check the userID able to approve or not: {}", userId);
        if (userClient.isStaff(userId).equals("Staff")) {
            return "userId " + userId + " able to make an approval";
        } else {
            return "userId " + userId + " unable to make an approval, please try again";
        }
    }
}
