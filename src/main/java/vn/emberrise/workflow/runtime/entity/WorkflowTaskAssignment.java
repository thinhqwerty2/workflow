package vn.emberrise.workflow.runtime.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "wf_task_assignment")
public class WorkflowTaskAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long activeTaskId;
    private String assigneeId;
    private Boolean isCompleted = false;
}