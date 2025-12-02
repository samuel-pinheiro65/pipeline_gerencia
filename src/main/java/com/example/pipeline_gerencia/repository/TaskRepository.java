package com.example.pipeline_gerencia.repository;

import com.example.pipeline_gerencia.model.Task;
import com.example.pipeline_gerencia.model.Status;
import com.example.pipeline_gerencia.model.Priority;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Task entity
 */
public interface TaskRepository {
    Task save(Task task);
    Optional<Task> findById(Long id);
    List<Task> findAll();
    List<Task> findByStatus(Status status);
    List<Task> findByPriority(Priority priority);
    List<Task> findByAssigneeId(Long userId);
    List<Task> findByCategoryId(Long categoryId);
    List<Task> findOverdueTasks();
    List<Task> findByDueDateBetween(LocalDateTime start, LocalDateTime end);
    void update(Task task);
    void delete(Long id);
}
