package vn.emberrise.workflow.definition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.emberrise.workflow.definition.entity.WorkflowEdge;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowEdgeRepository extends JpaRepository<WorkflowEdge, Long> {
    List<WorkflowEdge> findByWfId(Long wfId);
    
    // Tìm đường đi tiếp theo từ một nút dựa trên Action hoặc Kết quả logic
    @Query("SELECT e FROM WorkflowEdge e WHERE e.fromNodeId = :nodeId AND e.triggerValue = :val")
    Optional<WorkflowEdge> findNextEdge(Long nodeId, String val);

    // Tìm tất cả các cạnh nối đến một nút (Dùng cho logic JOIN)
    List<WorkflowEdge> findByToNodeId(Long toNodeId);
}