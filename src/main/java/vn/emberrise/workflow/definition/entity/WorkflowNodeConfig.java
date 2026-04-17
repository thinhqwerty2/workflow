package vn.emberrise.workflow.definition.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "wf_node_config")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class WorkflowNodeConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long nodeId;
    private String configKey;   // vd: "subflow_id", "api_url", "timeout"
    private String configValue; // Lưu tất cả dạng String
}