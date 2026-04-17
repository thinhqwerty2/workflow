package vn.emberrise.workflow.runtime.service.assignment;

import vn.emberrise.workflow.definition.constant.AssigneeType;

import java.util.List;
import java.util.Map;

public interface AssigneeStrategy {
    AssigneeType getType();
    List<Long> resolve(String value, Map<String, Object> context);
}
