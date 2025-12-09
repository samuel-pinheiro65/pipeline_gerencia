package com.example.pipeline_gerencia.unit;

import com.example.pipeline_gerencia.model.User;
import com.example.pipeline_gerencia.repository.UserRepository;
import com.example.pipeline_gerencia.service.UserService;
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
 * Testes unitários para UserService usando mocks
 */
@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    void testCreateUser() {
        User savedUser = new User("João Silva", "joao@example.com", "Desenvolvimento");
        savedUser.setId(1L);

        when(userRepository.findByEmail("joao@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.createUser("João Silva", "joao@example.com", "Desenvolvimento");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUserWithDuplicateEmail() {
        when(userRepository.findByEmail("joao@example.com")).thenReturn(Optional.of(new User("João", "joao@example.com", "Desenvolvimento")));

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("Maria", "joao@example.com", "Gerência");
        }, "Deve lançar exceção para email duplicado");

        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateUserWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("", "joao@example.com", "Desenvolvimento");
        }, "Deve lançar exceção para nome vazio");

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(null, "joao@example.com", "Desenvolvimento");
        }, "Deve lançar exceção para nome nulo");
    }

    @Test
    void testCreateUserWithEmptyEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("João", "", "Desenvolvimento");
        }, "Deve lançar exceção para email vazio");
    }

    @Test
    void testGetUserById() {
        User user = new User("João", "joao@example.com", "Desenvolvimento");
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserByEmail() {
        User user = new User("João", "joao@example.com", "Desenvolvimento");

        when(userRepository.findByEmail("joao@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByEmail("joao@example.com");

        assertTrue(result.isPresent());
        assertEquals("joao@example.com", result.get().getEmail());
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(
            new User("João", "joao@example.com", "Desenvolvimento"),
            new User("Maria", "maria@example.com", "Gerência")
        );

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUsersByDepartment() {
        List<User> users = Arrays.asList(
            new User("João", "joao@example.com", "Desenvolvimento")
        );

        when(userRepository.findByDepartment("Desenvolvimento")).thenReturn(users);

        List<User> result = userService.getUsersByDepartment("Desenvolvimento");

        assertEquals(1, result.size());
        verify(userRepository, times(1)).findByDepartment("Desenvolvimento");
    }

    @Test
    void testDeactivateUser() {
        User user = new User("João", "joao@example.com", "Desenvolvimento");
        user.setId(1L);
        user.setActive(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).update(any(User.class));

        userService.deactivateUser(1L);

        assertFalse(user.isActive());
        verify(userRepository, times(1)).update(user);
    }

    @Test
    void testUpdateUser() {
        User user = new User("João", "joao@example.com", "Desenvolvimento");
        user.setId(1L);

        doNothing().when(userRepository).update(any(User.class));

        User result = userService.updateUser(user);

        assertEquals(user, result);
        verify(userRepository, times(1)).update(user);
    }

    @Test
    void testUpdateUserWithoutId() {
        User user = new User("João", "joao@example.com", "Desenvolvimento");

        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(user);
        }, "Deve lançar exceção quando ID é nulo");
    }
}

