package com.example.pipeline_gerencia.unit;

import com.example.pipeline_gerencia.model.*;
import com.example.pipeline_gerencia.repository.TaskRepository;
import com.example.pipeline_gerencia.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para TaskService usando mocks
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceUnitTest {

    @Mock
    private TaskRepository taskRepository;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskRepository);
    }

    @Test
    void testCreateTask() {
        Task task = new Task("Nova Tarefa", "Descrição");
        Task savedTask = new Task("Nova Tarefa", "Descrição");
        savedTask.setId(1L);

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        Task result = taskService.createTask(task);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testCreateTaskWithInvalidTitle() {
        Task task = new Task("ab", "Descrição muito curta");

        assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask(task);
        }, "Deve lançar exceção para título inválido");

        verify(taskRepository, never()).save(any());
    }

    @Test
    void testGetTaskById() {
        Task task = new Task("Tarefa", "Descrição");
        task.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.getTaskById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllTasks() {
        List<Task> tasks = Arrays.asList(
            new Task("Tarefa 1", "Descrição 1"),
            new Task("Tarefa 2", "Descrição 2")
        );

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testUpdateStatus() {
        Task task = new Task("Tarefa", "Descrição");
        task.setId(1L);
        task.setStatus(Status.PENDING);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).update(any(Task.class));

        Task result = taskService.updateStatus(1L, Status.COMPLETED);

        assertEquals(Status.COMPLETED, result.getStatus());
        assertEquals(100, result.getCompletionPercentage());
        verify(taskRepository, times(1)).update(any(Task.class));
    }

    @Test
    void testUpdateStatusTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            taskService.updateStatus(1L, Status.COMPLETED);
        }, "Deve lançar exceção quando tarefa não encontrada");
    }

    @Test
    void testUpdatePriority() {
        Task task = new Task("Tarefa", "Descrição");
        task.setId(1L);
        task.setPriority(Priority.LOW);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).update(any(Task.class));

        Task result = taskService.updatePriority(1L, Priority.HIGH);

        assertEquals(Priority.HIGH, result.getPriority());
        verify(taskRepository, times(1)).update(any(Task.class));
    }

    @Test
    void testGetCompletionRate() {
        Task task1 = new Task("Tarefa 1", "Descrição");
        task1.setStatus(Status.COMPLETED);
        Task task2 = new Task("Tarefa 2", "Descrição");
        task2.setStatus(Status.PENDING);

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        int rate = taskService.getCompletionRate();

        assertEquals(50, rate, "Taxa de conclusão deve ser 50%");
    }

    @Test
    void testGetCompletionRateEmpty() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList());

        int rate = taskService.getCompletionRate();

        assertEquals(0, rate, "Taxa deve ser 0 quando não há tarefas");
    }

    @Test
    void testGetAverageCompletionPercentage() {
        Task task1 = new Task("Tarefa 1", "Descrição");
        task1.setCompletionPercentage(50);
        Task task2 = new Task("Tarefa 2", "Descrição");
        task2.setCompletionPercentage(100);

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        int average = taskService.getAverageCompletionPercentage();

        assertEquals(75, average, "Média deve ser 75%");
    }
}

