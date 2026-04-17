package vn.emberrise.workflow.runtime.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;
import vn.emberrise.workflow.definition.constant.InstanceStatus;
import vn.emberrise.workflow.definition.constant.NodeType;
import vn.emberrise.workflow.definition.constant.TaskStatus;
import vn.emberrise.workflow.definition.constant.TriggerType;
import vn.emberrise.workflow.definition.entity.WorkflowNodeApproverConfig;
import vn.emberrise.workflow.definition.entity.WorkflowEdge;
import vn.emberrise.workflow.definition.entity.WorkflowNode;
import vn.emberrise.workflow.definition.entity.WorkflowNodeConfig;
import vn.emberrise.workflow.definition.repository.WorkflowEdgeRepository;
import vn.emberrise.workflow.definition.repository.WorkflowNodeApproverConfigRepository;
import vn.emberrise.workflow.definition.repository.WorkflowNodeRepository;
import vn.emberrise.workflow.definition.utils.DefinitionWorkflowHelper;
import vn.emberrise.workflow.runtime.actionhandler.ActionHandler;
import vn.emberrise.workflow.runtime.actionhandler.ActionProvider;
import vn.emberrise.workflow.runtime.entity.WorkflowActiveTask;
import vn.emberrise.workflow.runtime.entity.WorkflowInstance;
import vn.emberrise.workflow.runtime.entity.WorkflowTaskAssignment;
import vn.emberrise.workflow.runtime.repository.WorkflowActiveTaskRepository;
import vn.emberrise.workflow.runtime.repository.WorkflowInstanceRepository;
import vn.emberrise.workflow.runtime.repository.WorkflowTaskAssignmentRepository;
import vn.emberrise.workflow.runtime.service.assignment.AssignmentResolver;
import vn.emberrise.workflow.runtime.utils.RuntimeWorkflowHelper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WorkflowEngine {
    private final ActionProvider actionProvider;
    private final WorkflowInstanceRepository instanceRepo;
    private final WorkflowActiveTaskRepository activeTaskRepo;
    private final WorkflowNodeRepository nodeRepo;
    private final WorkflowEdgeRepository edgeRepo;
    private final RuntimeWorkflowHelper runtimeWorkflowHelper;
    private final DefinitionWorkflowHelper definitionWorkflowHelper;
    private final WorkflowTaskAssignmentRepository workflowTaskAssignmentRepository;


    private final Logger log = LoggerFactory.getLogger(WorkflowEngine.class);
    private final WorkflowNodeApproverConfigRepository workflowNodeApproverConfigRepository;
    private final AssignmentResolver assignmentResolver;

    // Bắt đầu một quy trình mới
    @Transactional
    public WorkflowInstance startProcess(Long wfId, Map<String, Object> initialData) {
        WorkflowInstance instance = new WorkflowInstance();
        instance.setWfId(wfId);
        instance.setContextData(initialData);
        instance.setStatus(InstanceStatus.RUNNING);
        instance = instanceRepo.save(instance);

        // Tìm nút START
        WorkflowNode startNode = nodeRepo.findByWfIdAndNodeType(wfId, NodeType.START);

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
                task.setStatus(TaskStatus.PENDING);
                activeTaskRepo.save(task);

                // Phân quyền cho user cụ thể dựa trên context
                createTaskAssignments(instance, task);
                break;

            case END:
                completeInstance(instance);
                break;
        }
    }

    private void completeInstance(WorkflowInstance instance) {

        // 1. Kết thúc Instance hiện tại
        instance.setStatus(InstanceStatus.COMPLETED);
        instance.setEndTime(LocalDateTime.now());
        activeTaskRepo.deleteByInstanceId(instance.getId());
        instanceRepo.save(instance);

        // 2. Kiểm tra xem có phải là Subflow không
        if (instance.getParentInstanceId() != null) {
            log.info("Subflow {} hoàn thành. Đang quay lại quy trình cha {}",
                    instance.getId(), instance.getParentInstanceId());

            // 3. Đánh thức quy trình cha
            // Ta cần gọi executeNode cho Node TIẾP THEO của cái ParentNodeId
            Long nextNodeId = definitionWorkflowHelper.findNextNodeIdCommon(instance.getParentNodeId());
            WorkflowInstance parentInstance = instanceRepo.findById(instance.getParentInstanceId())
                    .orElseThrow(() -> new RuntimeException("Parent instance not found"));
            // Gọi lại Engine để chạy tiếp quy trình cha
            this.executeNode(parentInstance, nextNodeId);
        }
    }

    private void moveToNext(WorkflowInstance instance, Long currentNodeId, String triggerValue) {
        edgeRepo.findNextEdge(currentNodeId, triggerValue)
                .ifPresent(edge -> executeNode(instance, edge.getToNodeId()));
    }

    private void executeServiceLogic(WorkflowInstance instance, WorkflowNode node) {
        // 1. Lấy tất cả cấu hình của Node này (ví dụ: template_id, api_url...)
        Map<String, String> configs = definitionWorkflowHelper.getConfigByNode(node).stream()
                .collect(Collectors.toMap(WorkflowNodeConfig::getConfigKey, WorkflowNodeConfig::getConfigValue));

        // 2. Lấy dữ liệu context hiện tại của Instance
        Map<String, Object> context = runtimeWorkflowHelper.getContextMap(instance.getId());

        // 3. Tìm Handler tương ứng (Dùng Strategy Pattern hoặc Map Bean)
        // Giả sử bạn có một Map<String, ActionHandler> beanHandlers;
        ActionHandler handler = actionProvider.getHandler(node.getActionKey());

        if (handler != null) {
            handler.execute(instance, configs, context);
        }
    }

    private String evaluateGateway(WorkflowInstance instance, WorkflowNode node) {
        Map<String, Object> context = runtimeWorkflowHelper.getContextMap(instance.getId());

        // Lấy tất cả các cạnh đi ra từ Gateway này
        List<WorkflowEdge> edges = definitionWorkflowHelper.getAllEdgesByFromNodeId(node.getId());

        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext spelContext = new StandardEvaluationContext();
        spelContext.setVariables(context);

        for (WorkflowEdge edge : edges) {
            if (TriggerType.CONDITION.equals(edge.getTriggerType())) {
                // Kiểm tra biểu thức logic (ví dụ: #total_amount > 500)
                Boolean result = parser.parseExpression(edge.getTriggerValue()).getValue(spelContext, Boolean.class);
                if (Boolean.TRUE.equals(result)) {
                    return edge.getTriggerValue(); // Trả về giá trị của cạnh thỏa mãn
                }
            }
        }
        return "DEFAULT"; // Hoặc một giá trị mặc định nếu không khớp
    }

    private void createTaskAssignments(WorkflowInstance instance, WorkflowActiveTask task) {
        // Lấy danh sách ID đã tính toán được từ bước ASSIGNMENT trước đó
        String assigneeIdsStr = runtimeWorkflowHelper.getInstanceDataValue(instance.getId(), "PENDING_ASSIGNNEES_" + task.getNodeId());

        if (assigneeIdsStr != null) {
            String[] ids = assigneeIdsStr.split(",");
            for (String id : ids) {
                WorkflowTaskAssignment assignment = WorkflowTaskAssignment.builder()
                        .activeTaskId(task.getId())
                        .assigneeId(id)
                        .isCompleted(false)
                        .build();
                workflowTaskAssignmentRepository.save(assignment);
            }
        }
    }

    private void resolveAssignees(WorkflowInstance instance, WorkflowNode node) {
        // Lấy cấu hình Assignee (User, Role, hoặc Dept Head)
        List<WorkflowNodeApproverConfig> approverConfigs = workflowNodeApproverConfigRepository.findByNodeId(node.getId());

        for (WorkflowNodeApproverConfig config : approverConfigs) {
            List<Long> userIds = assignmentResolver.resolve(config.getAssigneeType(), config.getAssigneeValue(),
                    runtimeWorkflowHelper.getContextMap(instance.getId()));

            // Lưu danh sách User ID này vào một biến tạm trong context để USER_TASK sau đó sử dụng
            runtimeWorkflowHelper.saveInstanceData(instance, "PENDING_ASSIGNNEES_" + node.getId(), userIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")));

        }
    }

}