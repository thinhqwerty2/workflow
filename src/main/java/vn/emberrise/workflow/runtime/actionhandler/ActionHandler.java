package vn.emberrise.workflow.runtime.actionhandler;

import vn.emberrise.workflow.runtime.entity.WorkflowInstance;

import java.util.Map;

public interface ActionHandler {
    /**
     * @param instance: Lượt chạy hiện tại
     * @param configs: Các tham số cấu hình từ bảng wf_node_config (vd: email_template, api_endpoint)
     * @param context: Dữ liệu hiện tại của quy trình (đã được parse từ bảng wf_instance_data)
     */
    void execute(WorkflowInstance instance, Map<String, String> configs, Map<String, Object> context);
    
    /**
     * Trả về action_key duy nhất (vd: "SEND_EMAIL", "HIS_SYNC") 
     * dùng để khớp với action_key trong bảng wf_node
     */
    String getActionKey();
}