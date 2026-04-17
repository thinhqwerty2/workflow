package vn.emberrise.workflow.runtime.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "wf_instance_data_byte_array")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkflowInstanceDataByteArray {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "bytes", nullable = false)
    private byte[] bytes;

    private String name; // optional: debug/log

    private Long length; // optional: size

    private String mimeType; // optional: application/json, application/octet-stream
}
