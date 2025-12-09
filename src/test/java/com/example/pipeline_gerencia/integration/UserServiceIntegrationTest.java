package com.example.pipeline_gerencia.integration;

import com.example.pipeline_gerencia.model.User;
import com.example.pipeline_gerencia.repository.UserRepository;
import com.example.pipeline_gerencia.repository.impl.InMemoryUserRepository;
import com.example.pipeline_gerencia.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de integração para UserService com repositório real
 */
class UserServiceIntegrationTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        userService = new UserService(userRepository);
    }

    @Test
    void testCreateAndRetrieveUser() {
        User user = userService.createUser("João Silva", "joao@example.com", "Desenvolvimento");

        assertNotNull(user.getId());
        assertTrue(user.isActive());

        Optional<User> retrieved = userService.getUserById(user.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("João Silva", retrieved.get().getName());
        assertEquals("joao@example.com", retrieved.get().getEmail());
    }

    @Test
    void testCreateUserWithDuplicateEmail() {
        userService.createUser("João", "joao@example.com", "Desenvolvimento");

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("Maria", "joao@example.com", "Gerência");
        }, "Deve impedir criação de usuário com email duplicado");
    }

    @Test
    void testGetUserByEmail() {
        User user = userService.createUser("João Silva", "joao@example.com", "Desenvolvimento");

        Optional<User> found = userService.getUserByEmail("joao@example.com");
        assertTrue(found.isPresent());
        assertEquals(user.getId(), found.get().getId());
    }

    @Test
    void testGetUsersByDepartment() {
        userService.createUser("João", "joao@example.com", "Desenvolvimento");
        userService.createUser("Maria", "maria@example.com", "Desenvolvimento");
        userService.createUser("Pedro", "pedro@example.com", "Gerência");

        List<User> devUsers = userService.getUsersByDepartment("Desenvolvimento");
        assertEquals(2, devUsers.size(), "Deve encontrar 2 usuários do departamento Desenvolvimento");

        List<User> managementUsers = userService.getUsersByDepartment("Gerência");
        assertEquals(1, managementUsers.size(), "Deve encontrar 1 usuário do departamento Gerência");
    }

    @Test
    void testGetActiveUsers() {
        User user1 = userService.createUser("João", "joao@example.com", "Desenvolvimento");
        userService.createUser("Maria", "maria@example.com", "Desenvolvimento");

        List<User> activeUsers = userService.getActiveUsers();
        assertEquals(2, activeUsers.size(), "Deve ter 2 usuários ativos");

        userService.deactivateUser(user1.getId());

        activeUsers = userService.getActiveUsers();
        assertEquals(1, activeUsers.size(), "Deve ter 1 usuário ativo após desativar um");
    }

    @Test
    void testUpdateUser() {
        User user = userService.createUser("João", "joao@example.com", "Desenvolvimento");
        user.setName("João Silva");
        user.setDepartment("Gerência");

        User updated = userService.updateUser(user);

        assertEquals("João Silva", updated.getName());
        assertEquals("Gerência", updated.getDepartment());

        Optional<User> retrieved = userService.getUserById(user.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("João Silva", retrieved.get().getName());
    }

    @Test
    void testDeactivateUser() {
        User user = userService.createUser("João", "joao@example.com", "Desenvolvimento");
        assertTrue(user.isActive());

        userService.deactivateUser(user.getId());

        Optional<User> retrieved = userService.getUserById(user.getId());
        assertTrue(retrieved.isPresent());
        assertFalse(retrieved.get().isActive(), "Usuário deve estar desativado");
    }

    @Test
    void testDeleteUser() {
        User user = userService.createUser("João", "joao@example.com", "Desenvolvimento");
        Long userId = user.getId();

        assertTrue(userService.getUserById(userId).isPresent());

        userService.deleteUser(userId);

        assertFalse(userService.getUserById(userId).isPresent(), "Usuário deve ser removido");
    }

    @Test
    void testGetAllUsers() {
        userService.createUser("João", "joao@example.com", "Desenvolvimento");
        userService.createUser("Maria", "maria@example.com", "Gerência");
        userService.createUser("Pedro", "pedro@example.com", "QA");

        List<User> allUsers = userService.getAllUsers();
        assertEquals(3, allUsers.size(), "Deve retornar todos os usuários");
    }
}

