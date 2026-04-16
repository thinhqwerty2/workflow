package vn.emberrise.workflow.definition.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "wf_definition")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Integer version;

    private Boolean isActive = true;

}
