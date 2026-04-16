package vn.emberrise.workflow.runtime.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vn.emberrise.workflow.definition.constant.InstanceStatus;
import vn.emberrise.workflow.definition.entity.WorkflowNode;
import vn.emberrise.workflow.definition.entity.WorkflowNodeConfig;
import vn.emberrise.workflow.definition.repository.WorkflowEdgeRepository;
import vn.emberrise.workflow.definition.repository.WorkflowNodeRepository;
import vn.emberrise.workflow.runtime.entity.WorkflowActiveTask;
import vn.emberrise.workflow.runtime.entity.WorkflowInstance;
import vn.emberrise.workflow.runtime.repository.WorkflowActiveTaskRepository;
import vn.emberrise.workflow.runtime.repository.WorkflowInstanceRepository;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WorkflowEngine {
     private WorkflowInstanceRepository instanceRepo;
     private WorkflowActiveTaskRepository activeTaskRepo;
     private WorkflowNodeRepository nodeRepo;
     private WorkflowEdgeRepository edgeRepo;

    // Bắt đầu một quy trình mới
    @Transactional
    public WorkflowInstance startProcess(Long wfId, Map<String, Object> initialData) {
        WorkflowInstance instance = new WorkflowInstance();
        instance.setWfId(wfId);
        instance.setContextData(initialData);
        instance.setStatus(InstanceStatus.RUNNING);
        instance = instanceRepo.save(instance);

        // Tìm nút START
        WorkflowNode startNode = nodeRepo.findByWfIdAndNodeType(wfId, "START");
        
        // Bắt đầu chuỗi thực thi
        executeNode(instance, startNode.getId());
        return instance;
    }

    // Hàm thực thi đệ quy: Run-Until-Wait
    public void executeNode(WorkflowInstance instance, Long nodeId) {
        WorkflowNode node = nodeRepo.findById(nodeId).orElseThrow();

        switch (node.getNodeType()) {
            case SERVICE_TASK:
                // Gọi ActionHandler xử lý logic (Gửi mail, API...)
                executeServiceLogic(instance, node);
                moveToNext(instance, nodeId, "AUTO");
                break;

            case ASSIGNMENT:
                // Tính toán người duyệt và lưu vào context
                resolveAssignees(instance, node);
                moveToNext(instance, nodeId, "AUTO");
                break;

            case GATEWAY:
                // Tính toán biểu thức logic để tìm nhánh đi tiếp
                String condition = evaluateGateway(instance, node);
                moveToNext(instance, nodeId, condition);
                break;

            case USER_TASK:
                // TẠO ACTIVE TASK VÀ DỪNG LẠI
                WorkflowActiveTask task = new WorkflowActiveTask();
                task.setInstanceId(instance.getId());
                task.setNodeId(nodeId);
                activeTaskRepo.save(task);
                
                // Phân quyền cho user cụ thể dựa trên context
                createTaskAssignments(instance, task);
                break;

            case END:
                completeInstance(instance);
                break;
        }
    }

    private void moveToNext(WorkflowInstance instance, Long currentNodeId, String triggerValue) {
        edgeRepo.findNextEdge(currentNodeId, triggerValue)
                .ifPresent(edge -> executeNode(instance, edge.getToNodeId()));
    }

    private void executeServiceLogic(WorkflowInstance instance, WorkflowNode node) {
        // 1. Lấy tất cả cấu hình của Node này (ví dụ: template_id, api_url...)
        Map<String, String> configs = node.getConfigs().stream()
                .collect(Collectors.toMap(WorkflowNodeConfig::getConfigKey, WorkflowNodeConfig::getConfigValue));

        // 2. Lấy dữ liệu context hiện tại của Instance
        Map<String, Object> context = getContextMap(instance.getId());

        // 3. Tìm Handler tương ứng (Dùng Strategy Pattern hoặc Map Bean)
        // Giả sử bạn có một Map<String, ActionHandler> beanHandlers;
        ActionHandler handler = actionProvider.getHandler(node.getActionKey());

        if (handler != null) {
            handler.execute(instance, configs, context);
        }
    }

}