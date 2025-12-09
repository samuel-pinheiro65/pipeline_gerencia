package com.example.pipeline_gerencia.integration;

import com.example.pipeline_gerencia.model.*;
import com.example.pipeline_gerencia.repository.TaskRepository;
import com.example.pipeline_gerencia.repository.UserRepository;
import com.example.pipeline_gerencia.repository.impl.InMemoryTaskRepository;
import com.example.pipeline_gerencia.repository.impl.InMemoryUserRepository;
import com.example.pipeline_gerencia.service.TaskService;
import com.example.pipeline_gerencia.service.UserService;
import com.example.pipeline_gerencia.util.SearchFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de integração para TaskService com repositórios reais
 */
class TaskServiceIntegrationTest {

    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private TaskService taskService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        taskRepository = new InMemoryTaskRepository();
        userRepository = new InMemoryUserRepository();
        taskService = new TaskService(taskRepository);
        userService = new UserService(userRepository);
    }

    @Test
    void testCreateAndRetrieveTask() {
        // Criar usuário
        User user = userService.createUser("João Silva", "joao@example.com", "Desenvolvimento");

        // Criar tarefa
        Task task = new Task("Implementar API", "Criar endpoints REST");
        task.setAssignee(user);
        task.setPriority(Priority.HIGH);
        task.setDueDate(LocalDateTime.now().plusDays(5));

        Task savedTask = taskService.createTask(task);

        // Verificar persistência
        assertNotNull(savedTask.getId());
        Optional<Task> retrieved = taskService.getTaskById(savedTask.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("Implementar API", retrieved.get().getTitle());
        assertEquals(user.getId(), retrieved.get().getAssignee().getId());
    }

    @Test
    void testUpdateTaskStatusWorkflow() {
        User user = userService.createUser("Maria", "maria@example.com", "QA");
        Task task = new Task("Testar sistema", "Executar testes");
        task.setAssignee(user);
        Task savedTask = taskService.createTask(task);

        // Atualizar status
        Task updated = taskService.updateStatus(savedTask.getId(), Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, updated.getStatus());

        // Completar tarefa
        updated = taskService.updateStatus(savedTask.getId(), Status.COMPLETED);
        assertEquals(Status.COMPLETED, updated.getStatus());
        assertEquals(100, updated.getCompletionPercentage());

        // Verificar persistência
        Optional<Task> retrieved = taskService.getTaskById(savedTask.getId());
        assertTrue(retrieved.isPresent());
        assertEquals(Status.COMPLETED, retrieved.get().getStatus());
    }

    @Test
    void testSearchTasksByMultipleCriteria() {
        User user1 = userService.createUser("João", "joao@example.com", "Desenvolvimento");
        User user2 = userService.createUser("Maria", "maria@example.com", "QA");

        // Criar múltiplas tarefas
        Task task1 = new Task("Implementar API REST", "Criar endpoints");
        task1.setAssignee(user1);
        task1.setPriority(Priority.HIGH);
        task1.setStatus(Status.IN_PROGRESS);
        taskService.createTask(task1);

        Task task2 = new Task("Documentar API", "Adicionar Swagger");
        task2.setAssignee(user1);
        task2.setPriority(Priority.MEDIUM);
        task2.setStatus(Status.PENDING);
        taskService.createTask(task2);

        Task task3 = new Task("Testar API", "Testes de integração");
        task3.setAssignee(user2);
        task3.setPriority(Priority.HIGH);
        task3.setStatus(Status.IN_PROGRESS);
        taskService.createTask(task3);

        // Buscar por palavra-chave
        SearchFilter filter = new SearchFilter("API");
        List<Task> results = taskService.searchTasks(filter);
        assertEquals(3, results.size(), "Deve encontrar todas as tarefas com 'API'");

        // Buscar por prioridade
        filter = new SearchFilter();
        filter.setPriority(Priority.HIGH);
        results = taskService.searchTasks(filter);
        assertEquals(2, results.size(), "Deve encontrar 2 tarefas de alta prioridade");

        // Buscar por status
        filter = new SearchFilter();
        filter.setStatus(Status.IN_PROGRESS);
        results = taskService.searchTasks(filter);
        assertEquals(2, results.size(), "Deve encontrar 2 tarefas em andamento");

        // Buscar por atribuinte
        filter = new SearchFilter();
        filter.setAssigneeId(user1.getId());
        results = taskService.searchTasks(filter);
        assertEquals(2, results.size(), "Deve encontrar 2 tarefas do usuário 1");

        // Buscar combinando critérios
        filter = new SearchFilter("API");
        filter.setPriority(Priority.HIGH);
        filter.setStatus(Status.IN_PROGRESS);
        results = taskService.searchTasks(filter);
        assertEquals(2, results.size(), "Deve encontrar tarefas que atendem todos os critérios");
    }

    @Test
    void testOverdueTasksDetection() {
        User user = userService.createUser("João", "joao@example.com", "Desenvolvimento");

        // Tarefa atrasada
        Task overdueTask = new Task("Tarefa Atrasada", "Prazo vencido");
        overdueTask.setAssignee(user);
        overdueTask.setDueDate(LocalDateTime.now().minusDays(2));
        overdueTask.setStatus(Status.IN_PROGRESS);
        taskService.createTask(overdueTask);

        // Tarefa no prazo
        Task onTimeTask = new Task("Tarefa no Prazo", "Ainda há tempo");
        onTimeTask.setAssignee(user);
        onTimeTask.setDueDate(LocalDateTime.now().plusDays(5));
        onTimeTask.setStatus(Status.IN_PROGRESS);
        taskService.createTask(onTimeTask);

        // Tarefa concluída (não deve aparecer como atrasada)
        Task completedTask = new Task("Tarefa Concluída", "Já finalizada");
        completedTask.setAssignee(user);
        completedTask.setDueDate(LocalDateTime.now().minusDays(1));
        completedTask.setStatus(Status.COMPLETED);
        taskService.createTask(completedTask);

        List<Task> overdueTasks = taskService.getOverdueTasks();
        assertEquals(1, overdueTasks.size(), "Deve encontrar apenas 1 tarefa atrasada");
        assertEquals("Tarefa Atrasada", overdueTasks.get(0).getTitle());
    }

    @Test
    void testTaskCompletionStatistics() {
        User user = userService.createUser("João", "joao@example.com", "Desenvolvimento");

        // Criar tarefas com diferentes estados
        Task task1 = new Task("Tarefa 1", "Descrição");
        task1.setAssignee(user);
        task1.setCompletionPercentage(50);
        taskService.createTask(task1);

        Task task2 = new Task("Tarefa 2", "Descrição");
        task2.setAssignee(user);
        task2.setCompletionPercentage(100);
        task2.setStatus(Status.COMPLETED);
        taskService.createTask(task2);

        Task task3 = new Task("Tarefa 3", "Descrição");
        task3.setAssignee(user);
        task3.setCompletionPercentage(75);
        taskService.createTask(task3);

        // Verificar estatísticas
        int completionRate = taskService.getCompletionRate();
        assertEquals(33, completionRate, "Taxa de conclusão deve ser aproximadamente 33%");

        int averagePercentage = taskService.getAverageCompletionPercentage();
        assertEquals(75, averagePercentage, "Média de conclusão deve ser 75%");
    }

    @Test
    void testTasksDueInNextDays() {
        User user = userService.createUser("João", "joao@example.com", "Desenvolvimento");

        // Tarefa vencendo em 2 dias
        Task task1 = new Task("Tarefa 1", "Vence em 2 dias");
        task1.setAssignee(user);
        task1.setDueDate(LocalDateTime.now().plusDays(2));
        taskService.createTask(task1);

        // Tarefa vencendo em 5 dias
        Task task2 = new Task("Tarefa 2", "Vence em 5 dias");
        task2.setAssignee(user);
        task2.setDueDate(LocalDateTime.now().plusDays(5));
        taskService.createTask(task2);

        // Tarefa vencendo em 10 dias
        Task task3 = new Task("Tarefa 3", "Vence em 10 dias");
        task3.setAssignee(user);
        task3.setDueDate(LocalDateTime.now().plusDays(10));
        taskService.createTask(task3);

        List<Task> tasksDueInNextDays = taskService.getTasksDueInNextDays(7);
        assertEquals(2, tasksDueInNextDays.size(), "Deve encontrar tarefas vencendo nos próximos 7 dias");
    }

    @Test
    void testDeleteTask() {
        User user = userService.createUser("João", "joao@example.com", "Desenvolvimento");
        Task task = new Task("Tarefa para deletar", "Será removida");
        task.setAssignee(user);
        Task savedTask = taskService.createTask(task);

        Long taskId = savedTask.getId();
        assertTrue(taskService.getTaskById(taskId).isPresent());

        taskService.deleteTask(taskId);

        assertFalse(taskService.getTaskById(taskId).isPresent(), "Tarefa deve ser removida");
    }
}

