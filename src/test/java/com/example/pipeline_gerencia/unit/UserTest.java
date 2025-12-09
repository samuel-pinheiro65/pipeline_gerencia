package com.example.pipeline_gerencia.unit;

import com.example.pipeline_gerencia.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe User
 */
class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("João Silva", "joao@example.com", "Desenvolvimento");
    }

    @Test
    void testUserCreation() {
        assertNotNull(user);
        assertEquals("João Silva", user.getName());
        assertEquals("joao@example.com", user.getEmail());
        assertEquals("Desenvolvimento", user.getDepartment());
        assertTrue(user.isActive(), "Usuário deve estar ativo por padrão");
    }

    @Test
    void testUserDefaultConstructor() {
        User newUser = new User();
        assertNotNull(newUser);
        assertNull(newUser.getName());
        assertNull(newUser.getEmail());
        assertNull(newUser.getDepartment());
    }

    @Test
    void testUserSettersAndGetters() {
        user.setId(1L);
        user.setName("Maria Santos");
        user.setEmail("maria@example.com");
        user.setDepartment("Gerência");
        user.setActive(false);

        assertEquals(1L, user.getId());
        assertEquals("Maria Santos", user.getName());
        assertEquals("maria@example.com", user.getEmail());
        assertEquals("Gerência", user.getDepartment());
        assertFalse(user.isActive());
    }

    @Test
    void testUserToString() {
        String toString = user.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("User"));
        assertTrue(toString.contains("name"));
        assertTrue(toString.contains("email"));
    }
}

