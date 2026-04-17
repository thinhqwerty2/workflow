package vn.emberrise.workflow.runtime.actionhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.emberrise.workflow.runtime.entity.WorkflowInstance;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SendEmailNotificationHandler implements ActionHandler {


    @Override
    public void execute(WorkflowInstance instance, Map<String, String> configs, Map<String, Object> context) {
        // 1. Lấy config từ node định nghĩa
        String templateId = configs.get("template_id");

        // 2. Lấy dữ liệu thực tế từ context (EAV)
        String patientName = (String) context.get("patient_name");
        String result = (String) context.get("lab_result");

        // 3. Thực hiện logic nghiệp vụ
        System.out.println(patientName + result);
    }

    @Override
    public String getActionKey() {
        return "SEND_EMAIL_NOTIFICATION";
    }
}