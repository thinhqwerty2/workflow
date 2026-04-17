package vn.emberrise.workflow.runtime.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import vn.emberrise.workflow.definition.constant.InstanceStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "wf_instance")
@Getter
@Setter
public class WorkflowInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long wfId;
    private Long parentInstanceId;//Dùng cho subflow
    private Long parentNodeId;//Nút gọi subflow ở quy trình cha

    @Enumerated(EnumType.STRING)
    private InstanceStatus status; // RUNNING, SUSPENDED, COMPLETED, FAILED

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> contextData = new HashMap<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime endTime;
}