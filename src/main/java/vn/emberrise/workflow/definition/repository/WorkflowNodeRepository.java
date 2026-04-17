package vn.emberrise.workflow.definition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.emberrise.workflow.definition.constant.NodeType;
import vn.emberrise.workflow.definition.entity.WorkflowNode;

import java.util.List;

@Repository
public interface WorkflowNodeRepository extends JpaRepository<WorkflowNode, Long> {
    List<WorkflowNode> findByWfId(Long wfId);

    WorkflowNode findByWfIdAndNodeType(Long wfId, NodeType nodeType);
}