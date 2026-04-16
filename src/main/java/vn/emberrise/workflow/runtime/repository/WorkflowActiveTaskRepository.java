package vn.emberrise.workflow.runtime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.emberrise.workflow.runtime.entity.WorkflowActiveTask;

public interface WorkflowActiveTaskRepository extends JpaRepository<WorkflowActiveTask, Long> {
}
