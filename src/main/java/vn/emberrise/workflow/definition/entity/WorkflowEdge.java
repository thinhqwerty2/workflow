package vn.emberrise.workflow.definition.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.emberrise.workflow.definition.constant.TriggerType;

@Entity
@Table(name = "wf_edge")
@Getter
@Setter
public class WorkflowEdge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long wfId;
    
    private Long fromNodeId;
    private Long toNodeId;
    
    @Enumerated(EnumType.STRING)
    private TriggerType triggerType; // ACTION, CONDITION, AUTO
    
    private String triggerValue; // vd: "APPROVE" hoặc biểu thức logic
}