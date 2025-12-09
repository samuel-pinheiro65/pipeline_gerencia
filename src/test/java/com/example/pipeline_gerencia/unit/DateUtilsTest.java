package com.example.pipeline_gerencia.unit;

import com.example.pipeline_gerencia.util.DateUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe DateUtils
 */
class DateUtilsTest {

    @Test
    void testFormatDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 12, 15, 14, 30, 45);
        String formatted = DateUtils.formatDateTime(dateTime);
        
        assertNotNull(formatted);
        assertTrue(formatted.contains("15/12/2025"));
        assertTrue(formatted.contains("14:30:45"));
    }

    @Test
    void testFormatDateTimeNull() {
        String formatted = DateUtils.formatDateTime(null);
        assertNull(formatted, "Data nula deve retornar null");
    }

    @Test
    void testFormatDate() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 12, 15, 14, 30, 45);
        String formatted = DateUtils.formatDate(dateTime);
        
        assertNotNull(formatted);
        assertEquals("15/12/2025", formatted);
    }

    @Test
    void testFormatDateNull() {
        String formatted = DateUtils.formatDate(null);
        assertNull(formatted, "Data nula deve retornar null");
    }

    @Test
    void testGetDaysUntilDue() {
        LocalDateTime future = LocalDateTime.now().plusDays(5);
        long days = DateUtils.getDaysUntilDue(future);
        
        assertTrue(days > 0, "Dias até o prazo futuro deve ser positivo");
        assertTrue(days >= 4 && days <= 5, "Deve retornar aproximadamente 5 dias (pode variar devido ao tempo de execução)");
    }

    @Test
    void testGetDaysUntilDueNull() {
        long days = DateUtils.getDaysUntilDue(null);
        assertEquals(-1, days, "Data nula deve retornar -1");
    }

    @Test
    void testIsOverdue() {
        LocalDateTime past = LocalDateTime.now().minusDays(1);
        LocalDateTime future = LocalDateTime.now().plusDays(1);
        
        assertTrue(DateUtils.isOverdue(past), "Data no passado deve ser considerada atrasada");
        assertFalse(DateUtils.isOverdue(future), "Data no futuro não deve ser considerada atrasada");
    }

    @Test
    void testIsOverdueNull() {
        assertFalse(DateUtils.isOverdue(null), "Data nula não deve ser considerada atrasada");
    }

    @Test
    void testAddDays() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 12, 15, 14, 30, 45);
        LocalDateTime result = DateUtils.addDays(dateTime, 5);
        
        assertEquals(20, result.getDayOfMonth(), "Deve adicionar 5 dias");
        assertEquals(12, result.getMonthValue());
        assertEquals(2025, result.getYear());
    }

    @Test
    void testAddDaysNull() {
        LocalDateTime result = DateUtils.addDays(null, 5);
        assertNull(result, "Data nula deve retornar null");
    }

    @Test
    void testAddHours() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 12, 15, 14, 30, 45);
        LocalDateTime result = DateUtils.addHours(dateTime, 3);
        
        assertEquals(17, result.getHour(), "Deve adicionar 3 horas");
    }

    @Test
    void testAddHoursNull() {
        LocalDateTime result = DateUtils.addHours(null, 3);
        assertNull(result, "Data nula deve retornar null");
    }
}

