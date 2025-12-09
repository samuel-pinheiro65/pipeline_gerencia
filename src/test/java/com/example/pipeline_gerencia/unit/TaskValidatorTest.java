package com.example.pipeline_gerencia.unit;

import com.example.pipeline_gerencia.model.Task;
import com.example.pipeline_gerencia.util.TaskValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe TaskValidator
 */
class TaskValidatorTest {

    @Test
    void testValidTask() {
        Task task = new Task("Tarefa Válida", "Descrição válida");
        task.setCompletionPercentage(50);
        assertTrue(TaskValidator.isValid(task), "Tarefa válida deve passar na validação");
        assertNull(TaskValidator.getValidationError(task), "Não deve haver erro de validação");
    }

    @Test
    void testNullTask() {
        assertFalse(TaskValidator.isValid(null), "Tarefa nula deve ser inválida");
        assertEquals("Task cannot be null", TaskValidator.getValidationError(null));
    }

    @Test
    void testInvalidTitleTooShort() {
        Task task = new Task("ab", "Descrição válida");
        assertFalse(TaskValidator.isValidTitle(task.getTitle()), "Título muito curto deve ser inválido");
        assertNotNull(TaskValidator.getValidationError(task));
    }

    @Test
    void testInvalidTitleTooLong() {
        String longTitle = "a".repeat(256);
        assertFalse(TaskValidator.isValidTitle(longTitle), "Título muito longo deve ser inválido");
    }

    @Test
    void testInvalidTitleNull() {
        Task task = new Task();
        task.setDescription("Descrição válida");
        assertFalse(TaskValidator.isValidTitle(null), "Título nulo deve ser inválido");
    }

    @Test
    void testValidTitle() {
        assertTrue(TaskValidator.isValidTitle("Tarefa Válida"), "Título válido deve passar");
        assertTrue(TaskValidator.isValidTitle("abc"), "Título com 3 caracteres deve ser válido");
        assertTrue(TaskValidator.isValidTitle("a".repeat(255)), "Título com 255 caracteres deve ser válido");
    }

    @Test
    void testInvalidDescriptionTooLong() {
        String longDescription = "a".repeat(2001);
        Task task = new Task("Tarefa Válida", longDescription);
        assertFalse(TaskValidator.isValidDescription(task.getDescription()), "Descrição muito longa deve ser inválida");
        assertNotNull(TaskValidator.getValidationError(task));
    }

    @Test
    void testValidDescriptionNull() {
        assertTrue(TaskValidator.isValidDescription(null), "Descrição nula deve ser válida (opcional)");
    }

    @Test
    void testInvalidCompletionPercentage() {
        // Testar diretamente o validator com valor inválido
        assertFalse(TaskValidator.isValidCompletionPercentage(150), "Porcentagem acima de 100 deve ser inválida");
        assertFalse(TaskValidator.isValidCompletionPercentage(-1), "Porcentagem negativa deve ser inválida");
        
        // Testar que a Task limita o valor, mas o validator ainda detecta como inválido
        Task task = new Task("Tarefa Válida", "Descrição válida");
        task.setCompletionPercentage(150);
        // A tarefa limita a 100, mas o validator ainda deve detectar se passarmos 150 diretamente
        assertEquals(100, task.getCompletionPercentage(), "Task deve limitar a 100%");
    }

    @Test
    void testValidCompletionPercentage() {
        assertTrue(TaskValidator.isValidCompletionPercentage(0), "0% deve ser válido");
        assertTrue(TaskValidator.isValidCompletionPercentage(50), "50% deve ser válido");
        assertTrue(TaskValidator.isValidCompletionPercentage(100), "100% deve ser válido");
    }
}

