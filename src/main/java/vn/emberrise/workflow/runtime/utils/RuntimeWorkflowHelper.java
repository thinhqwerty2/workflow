package vn.emberrise.workflow.runtime.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.emberrise.workflow.runtime.constant.JsonDataType;
import vn.emberrise.workflow.runtime.entity.WorkflowInstance;
import vn.emberrise.workflow.runtime.entity.WorkflowInstanceData;
import vn.emberrise.workflow.runtime.repository.WorkflowInstanceDataRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RuntimeWorkflowHelper {
    private final WorkflowInstanceDataRepository instanceDataRepo;

    // Chuyển đổi dữ liệu từ bảng wf_instance_data sang Map để tính toán
    public Map<String, Object> getContextMap(Long instanceId) {
        List<WorkflowInstanceData> dataList = instanceDataRepo.findByInstanceId(instanceId);
        return dataList.stream().collect(Collectors.toMap(
                WorkflowInstanceData::getDataKey,
                d -> parseValue(d, d.getDataType())
        ));
    }

    public void saveInstanceData(WorkflowInstance instance, String key, String value) {
        WorkflowInstanceData data = instanceDataRepo.findByInstanceIdAndDataKey(instance.getId(), key)
                .orElse(new WorkflowInstanceData());
        data.setInstanceId(instance.getId());
        data.setDataKey(key);
        data.setDataValue(value);
        instanceDataRepo.save(data);
    }

    public Object parseValue(WorkflowInstanceData value, JsonDataType type) {
        if (value == null) return null;
        return switch (type) {
            case NUMBER -> Double.parseDouble(value.getDataValue());
            case BOOLEAN -> Boolean.parseBoolean(value.getDataValue());
            default -> value;
        };
    }

    public String getInstanceDataValue(Long instanceId, String dataKey) {
        var data = instanceDataRepo.findAllByInstanceIdAndDataKey(instanceId, dataKey);
        return data.getFirst().getDataValue();
    }
}
