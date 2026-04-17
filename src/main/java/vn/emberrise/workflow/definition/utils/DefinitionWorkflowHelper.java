package vn.emberrise.workflow.definition.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.emberrise.workflow.definition.entity.WorkflowEdge;
import vn.emberrise.workflow.definition.entity.WorkflowNode;
import vn.emberrise.workflow.definition.entity.WorkflowNodeConfig;
import vn.emberrise.workflow.definition.repository.WorkflowEdgeRepository;
import vn.emberrise.workflow.definition.repository.WorkflowNodeConfigRepository;
import vn.emberrise.workflow.definition.repository.WorkflowNodeRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DefinitionWorkflowHelper {

    private final WorkflowNodeConfigRepository workflowNodeConfigRepository;
    private final WorkflowNodeRepository workflowNodeRepository;
    private final WorkflowEdgeRepository workflowEdgeRepository;

    public List<WorkflowNodeConfig> getConfigByNode(WorkflowNode node) {
        return workflowNodeConfigRepository.findByNodeId(node.getId());
    }

    public List<WorkflowEdge> getAllEdgesByFromNodeId(Long nodeId) {
        return workflowEdgeRepository.findAllByFromNodeId(nodeId);
    }


    public Long findNextNodeIdCommon(Long nodeId) {
        return findNextNodeId(nodeId).getFirst();
    }

    public List<Long> findNextNodeIdGateway(Long nodeId) {
        return findNextNodeId(nodeId);
    }

    private List<Long> findNextNodeId(Long nodeId) {
        List<WorkflowEdge> edges = workflowEdgeRepository.findAllByFromNodeId(nodeId);
        return edges.stream().map(WorkflowEdge::getToNodeId).toList();
    }
}
