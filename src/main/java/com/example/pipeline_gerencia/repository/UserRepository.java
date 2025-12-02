package com.example.pipeline_gerencia.repository;

import com.example.pipeline_gerencia.model.User;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity
 */
public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    List<User> findByDepartment(String department);
    List<User> findActiveUsers();
    void update(User user);
    void delete(Long id);
}
