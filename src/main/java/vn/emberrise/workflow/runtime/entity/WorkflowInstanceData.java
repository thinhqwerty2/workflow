package vn.emberrise.workflow.runtime.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.emberrise.workflow.runtime.constant.JsonDataType;

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

    private String dataValue;

    @Enumerated(EnumType.STRING)
    private JsonDataType dataType;  // vd: "NUMBER", "STRING", "BOOLEAN","OBJECT","ARRAY" để convert ngược lại Java

    private String stringValue;
    private Double doubleValue;
    private Boolean booleanValue;

    private Integer arrayIndex;

    private Long byteArrayId;
}