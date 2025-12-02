package com.example.pipeline_gerencia.util;

import com.example.pipeline_gerencia.model.Priority;
import com.example.pipeline_gerencia.model.Status;

/**
 * Filter criteria for searching tasks
 */
public class SearchFilter {
    private String keyword;
    private Status status;
    private Priority priority;
    private Long assigneeId;
    private Long categoryId;
    private boolean showOverdueOnly;

    public SearchFilter() {}

    public SearchFilter(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isShowOverdueOnly() {
        return showOverdueOnly;
    }

    public void setShowOverdueOnly(boolean showOverdueOnly) {
        this.showOverdueOnly = showOverdueOnly;
    }

    public boolean hasFilters() {
        return keyword != null || status != null || priority != null || 
               assigneeId != null || categoryId != null || showOverdueOnly;
    }

    @Override
    public String toString() {
        return "SearchFilter{" +
                "keyword='" + keyword + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", assigneeId=" + assigneeId +
                ", categoryId=" + categoryId +
                ", showOverdueOnly=" + showOverdueOnly +
                '}';
    }
}
