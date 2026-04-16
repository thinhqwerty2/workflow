package vn.emberrise.workflow.runtime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.emberrise.workflow.runtime.entity.WorkflowInstance;

public interface WorkflowInstanceRepository extends JpaRepository<WorkflowInstance, Long> {
}
