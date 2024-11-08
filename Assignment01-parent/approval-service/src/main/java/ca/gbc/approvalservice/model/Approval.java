package ca.gbc.approvalservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "approval")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Approval {
    @Id
    private String approvalId;
    private Long userId;
    private String eventId;
    private boolean isApproved;
}
