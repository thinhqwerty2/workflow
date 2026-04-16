package vn.emberrise.workflow.definition.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import vn.emberrise.workflow.definition.constant.NodeType;

import java.util.Map;

@Entity
@Table(name = "wf_node")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long wfId;
    
    private String name;
    
    @Enumerated(EnumType.STRING)
    private NodeType nodeType; // START, END, USER_TASK, SERVICE_TASK, GATEWAY, SUBFLOW, ASSIGNMENT
    
    private String actionKey;

}