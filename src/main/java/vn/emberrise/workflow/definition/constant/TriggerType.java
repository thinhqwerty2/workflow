package vn.emberrise.workflow.definition.constant;

public enum TriggerType {
    ACTION,    // Do người dùng bấm nút
    CONDITION, // Do hệ thống tự tính toán (True/False)
    AUTO       // Tự động đi tiếp sau khi xong task
}