package vn.emberrise.workflow.runtime.service.assignment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.emberrise.workflow.definition.constant.AssigneeType;

import java.util.*;


@Component
@RequiredArgsConstructor
public class AssignmentResolver {

    private final Map<AssigneeType, AssigneeStrategy> strategies = new HashMap<>();

    // Constructor tự động đăng ký các chiến lược
    public AssignmentResolver(List<AssigneeStrategy> strategyList) {
        strategyList.forEach(s -> strategies.put(s.getType(), s));
    }

    /**
     * Hàm xử lý chính để tìm danh sách User ID
     *
     * @param type:    USER, ROLE, DEPT_HEAD, v.v.
     * @param value:   ID của User hoặc tên Role tương ứng
     * @param context: Dữ liệu thực tế của instance (EAV data)
     */
    public List<Long> resolve(AssigneeType type, String value, Map<String, Object> context) {
        AssigneeStrategy strategy = strategies.get(type);
        if (strategy == null) {
            return Collections.emptyList();
        }
        return strategy.resolve(value, context);
    }
}


