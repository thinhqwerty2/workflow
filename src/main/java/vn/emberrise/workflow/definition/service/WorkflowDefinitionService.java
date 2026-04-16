package vn.emberrise.workflow.definition.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.emberrise.workflow.definition.entity.WorkflowEdge;
import vn.emberrise.workflow.definition.entity.WorkflowNode;
import vn.emberrise.workflow.definition.repository.WorkflowEdgeRepository;
import vn.emberrise.workflow.definition.repository.WorkflowNodeRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class WorkflowDefinitionService {
    private WorkflowNodeRepository nodeRepo;
    private WorkflowEdgeRepository edgeRepo;

    @Transactional
    public void saveWorkflowDesign(Long wfId, List<WorkflowNode> nodes, List<WorkflowEdge> edges) {
        // 1. Xóa cấu hình cũ của bản vẽ này
//        nodeRepo.deleteByWfId(wfId);
//        edgeRepo.deleteByWfId(wfId);

        // 2. Lưu các nút mới
        nodeRepo.saveAll(nodes);

        // 3. Lưu các cạnh mới nối giữa các nút
        edgeRepo.saveAll(edges);
    }
}