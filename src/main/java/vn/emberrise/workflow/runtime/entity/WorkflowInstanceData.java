package vn.emberrise.workflow.runtime.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "wf_instance_data")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkflowInstanceData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long instanceId;

    private String dataKey;   // vd: "order_id"
    private String dataValue; // Lưu tất cả dưới dạng String
    private String dataType;  // vd: "NUMBER", "STRING", "BOOLEAN" để convert ngược lại Java
}