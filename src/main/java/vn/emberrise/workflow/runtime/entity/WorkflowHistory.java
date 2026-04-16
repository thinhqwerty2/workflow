package vn.emberrise.workflow.runtime.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "wf_history")
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long instanceId;
    private Long nodeId;
    private Long performerId;
    
    private String action; // vd: "APPROVE", "REJECT"
    private String comment;
    
    private LocalDateTime createdAt;
}