package com.example.pipeline_gerencia;

import com.example.pipeline_gerencia.model.*;
import com.example.pipeline_gerencia.repository.TaskRepository;
import com.example.pipeline_gerencia.repository.UserRepository;
import com.example.pipeline_gerencia.repository.impl.InMemoryTaskRepository;
import com.example.pipeline_gerencia.repository.impl.InMemoryUserRepository;
import com.example.pipeline_gerencia.service.TaskService;
import com.example.pipeline_gerencia.service.UserService;
import com.example.pipeline_gerencia.util.SearchFilter;
import com.example.pipeline_gerencia.util.TaskValidator;
import com.example.pipeline_gerencia.util.DateUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Comprehensive test suite for Task Manager application
 */
public class TaskManagerTests {

    public static void main(String[] args) {
        System.out.println("=== Task Manager Test Suite ===\n");

        // Initialize repositories and services
        UserRepository userRepository = new InMemoryUserRepository();
        TaskRepository taskRepository = new InMemoryTaskRepository();
        UserService userService = new UserService(userRepository);
        TaskService taskService = new TaskService(taskRepository);

        try {
            // Test 1: User Creation and Management
            System.out.println("TEST 1: User Creation and Management");
            testUserCreation(userService);

            // Test 2: Task Creation and Validation
            System.out.println("\nTEST 2: Task Creation and Validation");
            testTaskCreation(taskService, userService);

            // Test 3: Task Status Updates
            System.out.println("\nTEST 3: Task Status Updates");
            testTaskStatusUpdate(taskService, userService);

            // Test 4: Task Priority Management
            System.out.println("\nTEST 4: Task Priority Management");
            testTaskPriorityUpdate(taskService, userService);

            // Test 5: Task Search and Filtering
            System.out.println("\nTEST 5: Task Search and Filtering");
            testTaskSearching(taskService, userService);

            // Test 6: Overdue Task Detection
            System.out.println("\nTEST 6: Overdue Task Detection");
            testOverdueTaskDetection(taskService, userService);

            // Test 7: Task Completion Tracking
            System.out.println("\nTEST 7: Task Completion Tracking");
            testTaskCompletionTracking(taskService, userService);

            // Test 8: Validator Tests
            System.out.println("\nTEST 8: Task Validator");
            testTaskValidator();

            // Test 9: Date Utilities
            System.out.println("\nTEST 9: Date Utilities");
            testDateUtils();

            System.out.println("\n=== All Tests Completed Successfully ===");

        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testUserCreation(UserService userService) {
        // Create users
        User user1 = userService.createUser("João Silva", "joao@example.com", "Desenvolvimento");
        userService.createUser("Maria Santos", "maria@example.com", "Gerência");
        userService.createUser("Pedro Costa", "pedro@example.com", "Desenvolvimento");

        assert user1.getId() != null : "User ID should not be null";
        assert user1.isActive() : "User should be active by default";
        assert userService.getActiveUsers().size() == 3 : "Should have 3 active users";

        System.out.println("✓ Users created successfully: " + userService.getAllUsers().size() + " users");
    }

    private static void testTaskCreation(TaskService taskService, UserService userService) {
        User assignee = userService.createUser("Ana Silva", "ana@example.com", "Desenvolvimento");
        
        Task task1 = new Task("Implementar login", "Sistema de autenticação com JWT");
        task1.setAssignee(assignee);
        task1.setPriority(Priority.HIGH);
        task1.setDueDate(LocalDateTime.now().plusDays(3));
        
        Task savedTask = taskService.createTask(task1);
        assert savedTask.getId() != null : "Task ID should not be null";
        assert savedTask.getStatus() == Status.PENDING : "Task should be PENDING by default";
        assert savedTask.getCompletionPercentage() == 0 : "Completion should be 0% initially";

        System.out.println("✓ Task created: " + savedTask.getTitle());
    }

    private static void testTaskStatusUpdate(TaskService taskService, UserService userService) {
        User user = userService.createUser("Carlos Mendes", "carlos@example.com", "QA");
        
        Task task = new Task("Testar módulo de usuários", "Executar testes de integração");
        task.setAssignee(user);
        task.setPriority(Priority.MEDIUM);
        Task savedTask = taskService.createTask(task);

        // Update status to IN_PROGRESS
        Task updated = taskService.updateStatus(savedTask.getId(), Status.IN_PROGRESS);
        assert updated.getStatus() == Status.IN_PROGRESS : "Status should be IN_PROGRESS";

        // Update status to COMPLETED
        updated = taskService.updateStatus(savedTask.getId(), Status.COMPLETED);
        assert updated.getStatus() == Status.COMPLETED : "Status should be COMPLETED";
        assert updated.getCompletionPercentage() == 100 : "Completion should be 100% when completed";

        System.out.println("✓ Task status updated successfully");
    }

    private static void testTaskPriorityUpdate(TaskService taskService, UserService userService) {
        User user = userService.createUser("Lucas Santos", "lucas@example.com", "Desenvolvimento");
        
        Task task = new Task("Refatorar código antigo", "Limpar e otimizar");
        task.setAssignee(user);
        task.setPriority(Priority.LOW);
        Task savedTask = taskService.createTask(task);

        // Update priority
        Task updated = taskService.updatePriority(savedTask.getId(), Priority.CRITICAL);
        assert updated.getPriority() == Priority.CRITICAL : "Priority should be CRITICAL";

        System.out.println("✓ Task priority updated to: " + updated.getPriority());
    }

    private static void testTaskSearching(TaskService taskService, UserService userService) {
        User user = userService.createUser("Beatriz Costa", "beatriz@example.com", "Desenvolvimento");
        
        // Create multiple tasks
        Task task1 = new Task("Implementar API", "Criar endpoints REST");
        task1.setAssignee(user);
        task1.setPriority(Priority.HIGH);
        taskService.createTask(task1);

        Task task2 = new Task("Documentar API", "Adicionar comentários e Swagger");
        task2.setAssignee(user);
        task2.setPriority(Priority.MEDIUM);
        taskService.createTask(task2);

        Task task3 = new Task("Revisar código", "Code review antes do merge");
        task3.setAssignee(user);
        task3.setPriority(Priority.HIGH);
        taskService.createTask(task3);

        // Search by keyword
        SearchFilter filter = new SearchFilter("API");
        List<Task> results = taskService.searchTasks(filter);
        assert results.size() == 2 : "Should find 2 tasks with 'API'";

        // Search by priority
        filter = new SearchFilter();
        filter.setPriority(Priority.HIGH);
        results = taskService.searchTasks(filter);
        assert results.size() == 2 : "Should find 2 HIGH priority tasks";

        System.out.println("✓ Task search completed: Found " + results.size() + " tasks with filter");
    }

    private static void testOverdueTaskDetection(TaskService taskService, UserService userService) {
        User user = userService.createUser("Rafael Oliveira", "rafael@example.com", "Gerência");
        
        Task task = new Task("Tarefa atrasada", "Tarefa com prazo vencido");
        task.setAssignee(user);
        task.setDueDate(LocalDateTime.now().minusDays(2)); // 2 days ago
        Task savedTask = taskService.createTask(task);

        assert savedTask.isOverdue() : "Task should be marked as overdue";

        List<Task> overdueTasks = taskService.getOverdueTasks();
        assert overdueTasks.size() > 0 : "Should have at least one overdue task";

        System.out.println("✓ Overdue task detected: " + overdueTasks.size() + " overdue tasks");
    }

    private static void testTaskCompletionTracking(TaskService taskService, UserService userService) {
        User user = userService.createUser("Fernanda Lima", "fernanda@example.com", "Desenvolvimento");
        
        // Create tasks
        Task task1 = new Task("Tarefa 1", "Descrição 1");
        task1.setAssignee(user);
        task1.setCompletionPercentage(50);
        taskService.createTask(task1);

        Task task2 = new Task("Tarefa 2", "Descrição 2");
        task2.setAssignee(user);
        task2.setCompletionPercentage(100);
        task2.setStatus(Status.COMPLETED);
        taskService.createTask(task2);

        Task task3 = new Task("Tarefa 3", "Descrição 3");
        task3.setAssignee(user);
        task3.setCompletionPercentage(75);
        taskService.createTask(task3);

        int completionRate = taskService.getCompletionRate();
        int averagePercentage = taskService.getAverageCompletionPercentage();

        System.out.println("✓ Completion Rate: " + completionRate + "%");
        System.out.println("✓ Average Completion: " + averagePercentage + "%");
    }

    private static void testTaskValidator() {
        // Valid task
        Task validTask = new Task("Valid Task", "A valid task description");
        assert TaskValidator.isValid(validTask) : "Task should be valid";

        // Invalid title (too short)
        Task invalidTask = new Task("ab", "Valid description");
        assert !TaskValidator.isValidTitle(invalidTask.getTitle()) : "Title too short";

        // Invalid percentage
        Task taskBadPercentage = new Task("Task", "Description");
        taskBadPercentage.setCompletionPercentage(150);
        assert taskBadPercentage.getCompletionPercentage() == 100 : "Percentage should be capped at 100";

        System.out.println("✓ Validation tests passed");
    }

    private static void testDateUtils() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime future = now.plusDays(5);

        String formatted = DateUtils.formatDateTime(now);
        assert formatted != null && !formatted.isEmpty() : "Date should be formatted";

        long daysUntil = DateUtils.getDaysUntilDue(future);
        assert daysUntil > 0 : "Days until due should be positive";

        assert !DateUtils.isOverdue(future) : "Future date should not be overdue";
        assert DateUtils.isOverdue(now.minusDays(1)) : "Past date should be overdue";

        System.out.println("✓ Date utilities tested successfully");
    }
}
