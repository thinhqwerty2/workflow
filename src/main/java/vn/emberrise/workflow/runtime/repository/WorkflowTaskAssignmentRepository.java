package vn.emberrise.workflow.runtime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.emberrise.workflow.runtime.entity.WorkflowTaskAssignment;

import java.util.List;

@Repository
public interface WorkflowTaskAssignmentRepository extends JpaRepository<WorkflowTaskAssignment, Long> {

    // Lấy danh sách việc cần làm cho 1 bác sĩ
    @Query("SELECT ta FROM WorkflowTaskAssignment ta " +
            "JOIN WorkflowActiveTask at ON ta.activeTaskId = at.id " +
            "WHERE ta.assigneeId = :userId AND ta.isCompleted = false")
    List<WorkflowTaskAssignment> findPendingTasksByUser(String userId);
}