package vn.emberrise.workflow.definition.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import vn.emberrise.workflow.definition.constant.AssigneeType;

@Entity
@Table(name = "wf_node_approver_config")
@NoArgsConstructor
@AllArgsConstructor
public class NodeApproverConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long nodeId;

    @Enumerated(EnumType.STRING)
    private AssigneeType assigneeType; // USER, ROLE, DEPT_HEAD

    private String assigneeValue; // ID hoặc mã của đối tượng được phân công
}