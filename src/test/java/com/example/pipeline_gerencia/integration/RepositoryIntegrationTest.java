package com.example.pipeline_gerencia.integration;

import com.example.pipeline_gerencia.model.*;
import com.example.pipeline_gerencia.repository.CategoryRepository;
import com.example.pipeline_gerencia.repository.TaskRepository;
import com.example.pipeline_gerencia.repository.UserRepository;
import com.example.pipeline_gerencia.repository.impl.InMemoryCategoryRepository;
import com.example.pipeline_gerencia.repository.impl.InMemoryTaskRepository;
import com.example.pipeline_gerencia.repository.impl.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de integração para os repositórios
 */
class RepositoryIntegrationTest {

    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        taskRepository = new InMemoryTaskRepository();
        userRepository = new InMemoryUserRepository();
        categoryRepository = new InMemoryCategoryRepository();
    }

    @Test
    void testTaskRepositoryOperations() {
        // Criar usuário
        User user = new User("João", "joao@example.com", "Desenvolvimento");
        user = userRepository.save(user);

        // Criar categoria
        Category category = new Category("Desenvolvimento", "Tarefas de desenvolvimento");
        category = categoryRepository.save(category);

        // Criar tarefa
        Task task = new Task("Nova Tarefa", "Descrição");
        task.setAssignee(user);
        task.setCategory(category);
        task.setPriority(Priority.HIGH);
        task.setStatus(Status.IN_PROGRESS);
        task.setDueDate(LocalDateTime.now().plusDays(5));

        Task saved = taskRepository.save(task);
        assertNotNull(saved.getId());

        // Buscar por ID
        Optional<Task> found = taskRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Nova Tarefa", found.get().getTitle());

        // Buscar por status
        List<Task> inProgress = taskRepository.findByStatus(Status.IN_PROGRESS);
        assertEquals(1, inProgress.size());

        // Buscar por prioridade
        List<Task> highPriority = taskRepository.findByPriority(Priority.HIGH);
        assertEquals(1, highPriority.size());

        // Buscar por atribuinte
        List<Task> userTasks = taskRepository.findByAssigneeId(user.getId());
        assertEquals(1, userTasks.size());

        // Buscar por categoria
        List<Task> categoryTasks = taskRepository.findByCategoryId(category.getId());
        assertEquals(1, categoryTasks.size());

        // Atualizar
        task.setStatus(Status.COMPLETED);
        taskRepository.update(task);
        found = taskRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(Status.COMPLETED, found.get().getStatus());

        // Deletar
        taskRepository.delete(saved.getId());
        found = taskRepository.findById(saved.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void testUserRepositoryOperations() {
        User user = new User("Maria", "maria@example.com", "Gerência");
        User saved = userRepository.save(user);

        assertNotNull(saved.getId());

        // Buscar por ID
        Optional<User> found = userRepository.findById(saved.getId());
        assertTrue(found.isPresent());

        // Buscar por email
        Optional<User> byEmail = userRepository.findByEmail("maria@example.com");
        assertTrue(byEmail.isPresent());
        assertEquals(saved.getId(), byEmail.get().getId());

        // Buscar por departamento
        List<User> deptUsers = userRepository.findByDepartment("Gerência");
        assertEquals(1, deptUsers.size());

        // Buscar usuários ativos
        List<User> activeUsers = userRepository.findActiveUsers();
        assertEquals(1, activeUsers.size());

        // Desativar usuário
        user.setActive(false);
        userRepository.update(user);
        activeUsers = userRepository.findActiveUsers();
        assertEquals(0, activeUsers.size());

        // Deletar
        userRepository.delete(saved.getId());
        found = userRepository.findById(saved.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void testCategoryRepositoryOperations() {
        Category category = new Category("Teste", "Categoria de teste");
        Category saved = categoryRepository.save(category);

        assertNotNull(saved.getId());

        // Buscar por ID
        Optional<Category> found = categoryRepository.findById(saved.getId());
        assertTrue(found.isPresent());

        // Buscar por nome
        Optional<Category> byName = categoryRepository.findByName("Teste");
        assertTrue(byName.isPresent());
        assertEquals(saved.getId(), byName.get().getId());

        // Atualizar
        category.setName("Teste Atualizado");
        categoryRepository.update(category);
        found = categoryRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Teste Atualizado", found.get().getName());

        // Deletar
        categoryRepository.delete(saved.getId());
        found = categoryRepository.findById(saved.getId());
        assertFalse(found.isPresent());
    }
}

