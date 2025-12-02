package com.example.pipeline_gerencia.repository.impl;

import com.example.pipeline_gerencia.model.Task;
import com.example.pipeline_gerencia.model.Status;
import com.example.pipeline_gerencia.model.Priority;
import com.example.pipeline_gerencia.repository.TaskRepository;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * In-memory implementation of TaskRepository
 */
public class InMemoryTaskRepository implements TaskRepository {
    private final Map<Long, Task> tasks = new HashMap<>();
    private long nextId = 1;

    @Override
    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(nextId++);
        }
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Task> findByStatus(Status status) {
        return tasks.values().stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findByPriority(Priority priority) {
        return tasks.values().stream()
                .filter(t -> t.getPriority() == priority)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findByAssigneeId(Long userId) {
        return tasks.values().stream()
                .filter(t -> t.getAssignee() != null && t.getAssignee().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findByCategoryId(Long categoryId) {
        return tasks.values().stream()
                .filter(t -> t.getCategory() != null && t.getCategory().getId().equals(categoryId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findOverdueTasks() {
        return tasks.values().stream()
                .filter(Task::isOverdue)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findByDueDateBetween(LocalDateTime start, LocalDateTime end) {
        return tasks.values().stream()
                .filter(t -> t.getDueDate() != null && 
                            !t.getDueDate().isBefore(start) && 
                            !t.getDueDate().isAfter(end))
                .collect(Collectors.toList());
    }

    @Override
    public void update(Task task) {
        if (task.getId() != null && tasks.containsKey(task.getId())) {
            task.setUpdatedAt(LocalDateTime.now());
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void delete(Long id) {
        tasks.remove(id);
    }
}
