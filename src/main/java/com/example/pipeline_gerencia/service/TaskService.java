package com.example.pipeline_gerencia.service;

import com.example.pipeline_gerencia.model.Task;
import com.example.pipeline_gerencia.model.Status;
import com.example.pipeline_gerencia.model.Priority;
import com.example.pipeline_gerencia.repository.TaskRepository;
import com.example.pipeline_gerencia.util.SearchFilter;
import com.example.pipeline_gerencia.util.TaskValidator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for task management business logic
 */
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        String validationError = TaskValidator.getValidationError(task);
        if (validationError != null) {
            throw new IllegalArgumentException(validationError);
        }
        return taskRepository.save(task);
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> searchTasks(SearchFilter filter) {
        List<Task> results = taskRepository.findAll();

        if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
            String keyword = filter.getKeyword().toLowerCase();
            results = results.stream()
                    .filter(t -> t.getTitle().toLowerCase().contains(keyword) ||
                               t.getDescription() != null && t.getDescription().toLowerCase().contains(keyword))
                    .collect(Collectors.toList());
        }

        if (filter.getStatus() != null) {
            results = results.stream()
                    .filter(t -> t.getStatus() == filter.getStatus())
                    .collect(Collectors.toList());
        }

        if (filter.getPriority() != null) {
            results = results.stream()
                    .filter(t -> t.getPriority() == filter.getPriority())
                    .collect(Collectors.toList());
        }

        if (filter.getAssigneeId() != null) {
            results = results.stream()
                    .filter(t -> t.getAssignee() != null && t.getAssignee().getId().equals(filter.getAssigneeId()))
                    .collect(Collectors.toList());
        }

        if (filter.getCategoryId() != null) {
            results = results.stream()
                    .filter(t -> t.getCategory() != null && t.getCategory().getId().equals(filter.getCategoryId()))
                    .collect(Collectors.toList());
        }

        if (filter.isShowOverdueOnly()) {
            results = results.stream()
                    .filter(Task::isOverdue)
                    .collect(Collectors.toList());
        }

        return results;
    }

    public Task updateTask(Task task) {
        String validationError = TaskValidator.getValidationError(task);
        if (validationError != null) {
            throw new IllegalArgumentException(validationError);
        }
        taskRepository.update(task);
        return task;
    }

    public void deleteTask(Long id) {
        taskRepository.delete(id);
    }

    public Task updateStatus(Long taskId, Status newStatus) {
        Optional<Task> optTask = taskRepository.findById(taskId);
        if (optTask.isPresent()) {
            Task task = optTask.get();
            task.setStatus(newStatus);
            if (newStatus == Status.COMPLETED) {
                task.setCompletionPercentage(100);
            }
            taskRepository.update(task);
            return task;
        }
        throw new IllegalArgumentException("Task not found: " + taskId);
    }

    public Task updatePriority(Long taskId, Priority newPriority) {
        Optional<Task> optTask = taskRepository.findById(taskId);
        if (optTask.isPresent()) {
            Task task = optTask.get();
            task.setPriority(newPriority);
            taskRepository.update(task);
            return task;
        }
        throw new IllegalArgumentException("Task not found: " + taskId);
    }

    public List<Task> getTasksByStatus(Status status) {
        return taskRepository.findByStatus(status);
    }

    public List<Task> getTasksByPriority(Priority priority) {
        return taskRepository.findByPriority(priority);
    }

    public List<Task> getOverdueTasks() {
        return taskRepository.findOverdueTasks();
    }

    public List<Task> getTasksDueInNextDays(int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = now.plusDays(days);
        return taskRepository.findByDueDateBetween(now, endDate);
    }

    public int getCompletionRate() {
        List<Task> allTasks = taskRepository.findAll();
        if (allTasks.isEmpty()) {
            return 0;
        }
        long completedCount = allTasks.stream()
                .filter(t -> t.getStatus() == Status.COMPLETED)
                .count();
        return (int) ((completedCount * 100) / allTasks.size());
    }

    public int getAverageCompletionPercentage() {
        List<Task> allTasks = taskRepository.findAll();
        if (allTasks.isEmpty()) {
            return 0;
        }
        return (int) allTasks.stream()
                .mapToInt(Task::getCompletionPercentage)
                .average()
                .orElse(0);
    }
}
