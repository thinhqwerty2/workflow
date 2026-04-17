package vn.emberrise.workflow.runtime.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wf_task_assignment")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowTaskAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long activeTaskId;
    private String assigneeId;
    private Boolean isCompleted = false;
}