package com.example.pipeline_gerencia.unit;

import com.example.pipeline_gerencia.model.Priority;
import com.example.pipeline_gerencia.model.Status;
import com.example.pipeline_gerencia.util.SearchFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe SearchFilter
 */
class SearchFilterTest {

    private SearchFilter filter;

    @BeforeEach
    void setUp() {
        filter = new SearchFilter();
    }

    @Test
    void testDefaultConstructor() {
        assertNull(filter.getKeyword());
        assertNull(filter.getStatus());
        assertNull(filter.getPriority());
        assertNull(filter.getAssigneeId());
        assertNull(filter.getCategoryId());
        assertFalse(filter.isShowOverdueOnly());
    }

    @Test
    void testConstructorWithKeyword() {
        SearchFilter filterWithKeyword = new SearchFilter("teste");
        assertEquals("teste", filterWithKeyword.getKeyword());
    }

    @Test
    void testSettersAndGetters() {
        filter.setKeyword("busca");
        filter.setStatus(Status.COMPLETED);
        filter.setPriority(Priority.HIGH);
        filter.setAssigneeId(1L);
        filter.setCategoryId(2L);
        filter.setShowOverdueOnly(true);

        assertEquals("busca", filter.getKeyword());
        assertEquals(Status.COMPLETED, filter.getStatus());
        assertEquals(Priority.HIGH, filter.getPriority());
        assertEquals(1L, filter.getAssigneeId());
        assertEquals(2L, filter.getCategoryId());
        assertTrue(filter.isShowOverdueOnly());
    }

    @Test
    void testHasFilters() {
        assertFalse(filter.hasFilters(), "Filtro vazio não deve ter filtros");

        filter.setKeyword("teste");
        assertTrue(filter.hasFilters(), "Deve ter filtros quando keyword é definido");

        filter = new SearchFilter();
        filter.setStatus(Status.PENDING);
        assertTrue(filter.hasFilters(), "Deve ter filtros quando status é definido");

        filter = new SearchFilter();
        filter.setPriority(Priority.HIGH);
        assertTrue(filter.hasFilters(), "Deve ter filtros quando priority é definido");

        filter = new SearchFilter();
        filter.setAssigneeId(1L);
        assertTrue(filter.hasFilters(), "Deve ter filtros quando assigneeId é definido");

        filter = new SearchFilter();
        filter.setCategoryId(1L);
        assertTrue(filter.hasFilters(), "Deve ter filtros quando categoryId é definido");

        filter = new SearchFilter();
        filter.setShowOverdueOnly(true);
        assertTrue(filter.hasFilters(), "Deve ter filtros quando showOverdueOnly é true");
    }

    @Test
    void testToString() {
        filter.setKeyword("teste");
        String toString = filter.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("SearchFilter"));
        assertTrue(toString.contains("teste"));
    }
}

