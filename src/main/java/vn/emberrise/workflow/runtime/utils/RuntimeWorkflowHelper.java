package vn.emberrise.workflow.runtime.utils;

import org.springframework.stereotype.Component;
import vn.emberrise.workflow.runtime.entity.WorkflowInstance;
import vn.emberrise.workflow.runtime.entity.WorkflowInstanceData;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RuntimeWorkflowHelper {
    // Chuyển đổi dữ liệu từ bảng wf_instance_data sang Map để tính toán
    private Map<String, Object> getContextMap(Long instanceId) {
        List<WorkflowInstanceData> dataList = instanceDataRepo.findByInstanceId(instanceId);
        return dataList.stream().collect(Collectors.toMap(
                WorkflowInstanceData::getDataKey,
                d -> parseValue(d.getDataValue(), d.getDataType())
        ));
    }

    private void saveInstanceData(WorkflowInstance instance, String key, String value) {
        WorkflowInstanceData data = instanceDataRepo.findByInstanceIdAndDataKey(instance.getId(), key)
                .orElse(new WorkflowInstanceData());
        data.setInstance(instance);
        data.setDataKey(key);
        data.setDataValue(value);
        instanceDataRepo.save(data);
    }

    private Object parseValue(String value, String type) {
        if (value == null) return null;
        return switch (type) {
            case "NUMBER" -> Double.parseDouble(value);
            case "BOOLEAN" -> Boolean.parseBoolean(value);
            default -> value;
        };
    }
}
