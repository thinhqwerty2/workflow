package vn.emberrise.workflow.runtime.service.assignment;

import org.springframework.stereotype.Component;
import vn.emberrise.workflow.definition.constant.AssigneeType;

import java.util.List;
import java.util.Map;

// --- 1. Chiến lược gán cho User cụ thể ---
@Component
class UserAssigneeStrategy implements AssigneeStrategy {
    @Override
    public AssigneeType getType() {
        return AssigneeType.USER;
    }

    @Override
    public List<Long> resolve(String value, Map<String, Object> context) {
        return List.of(Long.parseLong(value));
    }
}
