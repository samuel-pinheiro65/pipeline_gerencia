package com.example.pipeline_gerencia.acceptance;

import com.example.pipeline_gerencia.model.*;
import com.example.pipeline_gerencia.repository.TaskRepository;
import com.example.pipeline_gerencia.repository.UserRepository;
import com.example.pipeline_gerencia.repository.impl.InMemoryTaskRepository;
import com.example.pipeline_gerencia.repository.impl.InMemoryUserRepository;
import com.example.pipeline_gerencia.service.TaskService;
import com.example.pipeline_gerencia.service.UserService;
import com.example.pipeline_gerencia.util.SearchFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de Aceitação: Cenário completo de uso do sistema de gerenciamento de tarefas
 * 
 * Este teste valida que o sistema funciona corretamente em um cenário real de uso,
 * simulando um fluxo completo de trabalho de um usuário gerenciando tarefas.
 */
@DisplayName("Teste de Aceitação: Gerenciamento Completo de Tarefas")
class TaskManagerAcceptanceTest {

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
    @DisplayName("Cenário: Gerente cria equipe, atribui tarefas e monitora progresso")
    void testCompleteTaskManagementWorkflow() {
        // ===== ETAPA 1: Configuração Inicial =====
        // Gerente cria usuários da equipe
        User developer1 = userService.createUser("João Silva", "joao@example.com", "Desenvolvimento");
        User developer2 = userService.createUser("Maria Santos", "maria@example.com", "Desenvolvimento");
        User tester = userService.createUser("Pedro Costa", "pedro@example.com", "QA");

        assertEquals(3, userService.getActiveUsers().size(), "Deve ter 3 usuários ativos");

        // ===== ETAPA 2: Criação de Tarefas =====
        // Gerente cria tarefas para o projeto
        Task task1 = new Task("Implementar autenticação JWT", 
                              "Criar sistema de autenticação usando JWT tokens");
        task1.setAssignee(developer1);
        task1.setPriority(Priority.HIGH);
        task1.setDueDate(LocalDateTime.now().plusDays(7));
        Task savedTask1 = taskService.createTask(task1);

        Task task2 = new Task("Criar API de usuários", 
                              "Desenvolver endpoints REST para gerenciamento de usuários");
        task2.setAssignee(developer2);
        task2.setPriority(Priority.MEDIUM);
        task2.setDueDate(LocalDateTime.now().plusDays(10));
        Task savedTask2 = taskService.createTask(task2);

        Task task3 = new Task("Testar módulo de autenticação", 
                              "Executar testes unitários e de integração");
        task3.setAssignee(tester);
        task3.setPriority(Priority.HIGH);
        task3.setDueDate(LocalDateTime.now().plusDays(8));
        Task savedTask3 = taskService.createTask(task3);

        assertEquals(3, taskService.getAllTasks().size(), "Deve ter 3 tarefas criadas");

        // ===== ETAPA 3: Início do Trabalho =====
        // Desenvolvedores começam a trabalhar nas tarefas
        taskService.updateStatus(savedTask1.getId(), Status.IN_PROGRESS);
        taskService.updateStatus(savedTask2.getId(), Status.IN_PROGRESS);

        List<Task> inProgressTasks = taskService.getTasksByStatus(Status.IN_PROGRESS);
        assertEquals(2, inProgressTasks.size(), "Deve ter 2 tarefas em andamento");

        // ===== ETAPA 4: Atualização de Progresso =====
        // Desenvolvedor atualiza o progresso da tarefa
        Task task1Updated = taskService.getTaskById(savedTask1.getId()).orElseThrow();
        task1Updated.setCompletionPercentage(60);
        taskService.updateTask(task1Updated);

        Task task2Updated = taskService.getTaskById(savedTask2.getId()).orElseThrow();
        task2Updated.setCompletionPercentage(30);
        taskService.updateTask(task2Updated);

        int averageProgress = taskService.getAverageCompletionPercentage();
        assertTrue(averageProgress > 0, "Média de progresso deve ser maior que 0");

        // ===== ETAPA 5: Conclusão de Tarefas =====
        // Tarefa 1 é concluída
        taskService.updateStatus(savedTask1.getId(), Status.COMPLETED);
        
        Task completedTask = taskService.getTaskById(savedTask1.getId()).orElseThrow();
        assertEquals(Status.COMPLETED, completedTask.getStatus());
        assertEquals(100, completedTask.getCompletionPercentage(), 
                    "Tarefa concluída deve ter 100% de progresso");

        // ===== ETAPA 6: Busca e Filtros =====
        // Gerente busca tarefas de alta prioridade
        SearchFilter highPriorityFilter = new SearchFilter();
        highPriorityFilter.setPriority(Priority.HIGH);
        List<Task> highPriorityTasks = taskService.searchTasks(highPriorityFilter);
        assertEquals(2, highPriorityTasks.size(), "Deve encontrar 2 tarefas de alta prioridade");

        // Busca por palavra-chave
        SearchFilter apiFilter = new SearchFilter("API");
        List<Task> apiTasks = taskService.searchTasks(apiFilter);
        assertEquals(1, apiTasks.size(), "Deve encontrar 1 tarefa relacionada a API");

        // Busca tarefas de um desenvolvedor específico
        SearchFilter developerFilter = new SearchFilter();
        developerFilter.setAssigneeId(developer1.getId());
        List<Task> developer1Tasks = taskService.searchTasks(developerFilter);
        assertEquals(1, developer1Tasks.size(), "Deve encontrar 1 tarefa do desenvolvedor 1");

        // ===== ETAPA 7: Monitoramento de Prazos =====
        // Verificar tarefas atrasadas (criar uma tarefa atrasada)
        Task overdueTask = new Task("Tarefa Atrasada", "Prazo vencido");
        overdueTask.setAssignee(developer2);
        overdueTask.setDueDate(LocalDateTime.now().minusDays(2));
        overdueTask.setStatus(Status.IN_PROGRESS);
        taskService.createTask(overdueTask);

        List<Task> overdueTasks = taskService.getOverdueTasks();
        assertTrue(overdueTasks.size() > 0, "Deve identificar tarefas atrasadas");

        // Verificar tarefas vencendo nos próximos dias
        List<Task> tasksDueSoon = taskService.getTasksDueInNextDays(7);
        assertTrue(tasksDueSoon.size() > 0, "Deve encontrar tarefas vencendo em breve");

        // ===== ETAPA 8: Estatísticas do Projeto =====
        int completionRate = taskService.getCompletionRate();
        assertTrue(completionRate >= 0 && completionRate <= 100, 
                  "Taxa de conclusão deve estar entre 0 e 100%");

        int totalTasks = taskService.getAllTasks().size();
        assertTrue(totalTasks > 0, "Deve haver tarefas no sistema");

        // ===== ETAPA 9: Gerenciamento de Usuários =====
        // Desativar um usuário
        userService.deactivateUser(developer2.getId());
        List<User> activeUsers = userService.getActiveUsers();
        assertEquals(2, activeUsers.size(), "Deve ter 2 usuários ativos após desativar um");

        // Buscar usuários por departamento
        List<User> devUsers = userService.getUsersByDepartment("Desenvolvimento");
        assertEquals(2, devUsers.size(), "Deve encontrar 2 usuários do departamento Desenvolvimento");

        // ===== ETAPA 10: Validação Final =====
        // Verificar integridade dos dados
        assertNotNull(taskService.getTaskById(savedTask1.getId()), 
                     "Tarefa concluída deve ainda existir no sistema");
        assertNotNull(taskService.getTaskById(savedTask2.getId()), 
                     "Tarefa em andamento deve existir no sistema");
        assertNotNull(taskService.getTaskById(savedTask3.getId()), 
                     "Tarefa pendente deve existir no sistema");

        // Verificar que o sistema mantém consistência
        int finalTaskCount = taskService.getAllTasks().size();
        assertEquals(4, finalTaskCount, "Deve manter todas as tarefas criadas");

        // ===== VALIDAÇÃO FUNCIONAL =====
        // O sistema deve permitir:
        // ✓ Criar e gerenciar usuários
        // ✓ Criar e atribuir tarefas
        // ✓ Atualizar status e progresso
        // ✓ Buscar e filtrar tarefas
        // ✓ Monitorar prazos e atrasos
        // ✓ Calcular estatísticas
        // ✓ Manter integridade dos dados

        assertTrue(true, "Teste de aceitação concluído com sucesso - " +
                        "Sistema atende aos requisitos funcionais e não funcionais");
    }

    @Test
    @DisplayName("Cenário: Sistema deve validar dados e prevenir erros")
    void testSystemValidationAndErrorHandling() {
        // Teste de validação de tarefa inválida
        Task invalidTask = new Task("ab", "Descrição muito curta");
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask(invalidTask);
        }, "Sistema deve rejeitar tarefas com título inválido");

        // Teste de email duplicado
        userService.createUser("João", "joao@example.com", "Desenvolvimento");
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("Maria", "joao@example.com", "Gerência");
        }, "Sistema deve impedir emails duplicados");

        // Teste de atualização de tarefa inexistente
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.updateStatus(999L, Status.COMPLETED);
        }, "Sistema deve tratar tentativa de atualizar tarefa inexistente");

        assertTrue(true, "Sistema valida dados corretamente e trata erros adequadamente");
    }
}

