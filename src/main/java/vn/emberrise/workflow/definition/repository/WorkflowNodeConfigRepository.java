package vn.emberrise.workflow.definition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.emberrise.workflow.definition.entity.WorkflowNodeConfig;

import java.util.List;

@Repository
public interface WorkflowNodeConfigRepository extends JpaRepository<WorkflowNodeConfig, Long> {
    List<WorkflowNodeConfig> findByNodeId(Long nodeId);
}
