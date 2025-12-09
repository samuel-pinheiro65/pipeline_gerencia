package com.example.pipeline_gerencia.unit;

import com.example.pipeline_gerencia.model.Priority;
import com.example.pipeline_gerencia.model.Status;
import com.example.pipeline_gerencia.model.Task;
import com.example.pipeline_gerencia.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe Task
 */
class TaskTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task("Tarefa de Teste", "Descrição da tarefa");
    }

    @Test
    void testTaskCreation() {
        assertNotNull(task);
        assertEquals("Tarefa de Teste", task.getTitle());
        assertEquals("Descrição da tarefa", task.getDescription());
        assertEquals(Status.PENDING, task.getStatus());
        assertEquals(Priority.MEDIUM, task.getPriority());
        assertEquals(0, task.getCompletionPercentage());
        assertNotNull(task.getCreatedAt());
    }

    @Test
    void testTaskDefaultValues() {
        Task newTask = new Task();
        assertEquals(Status.PENDING, newTask.getStatus());
        assertEquals(Priority.MEDIUM, newTask.getPriority());
        assertEquals(0, newTask.getCompletionPercentage());
        assertNotNull(newTask.getCreatedAt());
    }

    @Test
    void testSetCompletionPercentage() {
        task.setCompletionPercentage(50);
        assertEquals(50, task.getCompletionPercentage());

        task.setCompletionPercentage(150);
        assertEquals(100, task.getCompletionPercentage(), "Deve limitar a 100%");

        task.setCompletionPercentage(-10);
        assertEquals(0, task.getCompletionPercentage(), "Deve limitar a 0%");
    }

    @Test
    void testIsOverdue() {
        // Tarefa com prazo no passado
        task.setDueDate(LocalDateTime.now().minusDays(1));
        task.setStatus(Status.IN_PROGRESS);
        assertTrue(task.isOverdue(), "Tarefa vencida deve ser identificada como atrasada");

        // Tarefa com prazo no futuro
        task.setDueDate(LocalDateTime.now().plusDays(1));
        assertFalse(task.isOverdue(), "Tarefa com prazo futuro não deve ser atrasada");

        // Tarefa concluída não é atrasada
        task.setDueDate(LocalDateTime.now().minusDays(1));
        task.setStatus(Status.COMPLETED);
        assertFalse(task.isOverdue(), "Tarefa concluída não deve ser considerada atrasada");

        // Tarefa sem prazo
        task.setDueDate(null);
        task.setStatus(Status.IN_PROGRESS);
        assertFalse(task.isOverdue(), "Tarefa sem prazo não deve ser considerada atrasada");
    }

    @Test
    void testTaskSettersAndGetters() {
        User user = new User("João", "joao@example.com", "Desenvolvimento");
        user.setId(1L);

        task.setId(1L);
        task.setTitle("Nova Tarefa");
        task.setDescription("Nova Descrição");
        task.setStatus(Status.IN_PROGRESS);
        task.setPriority(Priority.HIGH);
        task.setAssignee(user);
        task.setDueDate(LocalDateTime.now().plusDays(5));
        task.setUpdatedAt(LocalDateTime.now());

        assertEquals(1L, task.getId());
        assertEquals("Nova Tarefa", task.getTitle());
        assertEquals("Nova Descrição", task.getDescription());
        assertEquals(Status.IN_PROGRESS, task.getStatus());
        assertEquals(Priority.HIGH, task.getPriority());
        assertEquals(user, task.getAssignee());
        assertNotNull(task.getDueDate());
        assertNotNull(task.getUpdatedAt());
    }

    @Test
    void testTaskToString() {
        String toString = task.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Task"));
        assertTrue(toString.contains("title"));
    }
}

