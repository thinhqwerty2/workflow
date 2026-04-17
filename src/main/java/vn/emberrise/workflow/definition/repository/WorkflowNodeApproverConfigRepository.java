package vn.emberrise.workflow.definition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.emberrise.workflow.definition.entity.WorkflowNodeApproverConfig;

import java.util.List;

public interface WorkflowNodeApproverConfigRepository extends JpaRepository<WorkflowNodeApproverConfig,Long> {
    List<WorkflowNodeApproverConfig> findByNodeId(Long nodeId);
}
