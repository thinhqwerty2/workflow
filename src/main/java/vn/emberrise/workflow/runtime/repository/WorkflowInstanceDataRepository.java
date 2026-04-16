package vn.emberrise.workflow.runtime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.emberrise.workflow.runtime.entity.WorkflowInstance;
import vn.emberrise.workflow.runtime.entity.WorkflowInstanceData;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowInstanceDataRepository extends JpaRepository<WorkflowInstanceData, Long> {

    // Tìm quy trình dựa trên một giá trị cụ thể (vd: Tìm theo mã bệnh nhân)
    @Query("""
            SELECT wi FROM WorkflowInstanceData  d
            join WorkflowInstance wi on wi.id = d.instanceId
            WHERE d.dataKey = :key AND d.dataValue = :value
            """)
    List<WorkflowInstance> findByAttribute(String key, String value);

    // Xóa/Cập nhật dữ liệu context khi quy trình chạy
    Optional<WorkflowInstanceData> findByInstanceIdAndDataKey(Long instanceId, String dataKey);
}