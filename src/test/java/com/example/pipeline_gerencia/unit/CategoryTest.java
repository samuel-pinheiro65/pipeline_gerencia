package com.example.pipeline_gerencia.unit;

import com.example.pipeline_gerencia.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe Category
 */
class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category("Desenvolvimento", "Tarefas relacionadas ao desenvolvimento");
    }

    @Test
    void testCategoryCreation() {
        assertNotNull(category);
        assertEquals("Desenvolvimento", category.getName());
        assertEquals("Tarefas relacionadas ao desenvolvimento", category.getDescription());
        assertEquals("#000000", category.getColor(), "Cor padrão deve ser preta");
    }

    @Test
    void testCategoryDefaultConstructor() {
        Category newCategory = new Category();
        assertNotNull(newCategory);
        assertNull(newCategory.getName());
        assertNull(newCategory.getDescription());
    }

    @Test
    void testCategorySettersAndGetters() {
        category.setId(1L);
        category.setName("Teste");
        category.setDescription("Categoria de teste");
        category.setColor("#FF0000");

        assertEquals(1L, category.getId());
        assertEquals("Teste", category.getName());
        assertEquals("Categoria de teste", category.getDescription());
        assertEquals("#FF0000", category.getColor());
    }

    @Test
    void testCategoryToString() {
        String toString = category.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Category"));
        assertTrue(toString.contains("name"));
    }
}

