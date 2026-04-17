package vn.emberrise.workflow.runtime.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.emberrise.workflow.definition.constant.TaskStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "wf_active_task")
@Getter
@Setter
public class WorkflowActiveTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long instanceId;
    private Long nodeId;
    @Enumerated(EnumType.STRING)
    private TaskStatus status; // PENDING, IN_PROGRESS, COMPLETED
    private LocalDateTime createdAt;
}