package com.example.pipeline_gerencia.repository;

import com.example.pipeline_gerencia.model.Category;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Category entity
 */
public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findById(Long id);
    Optional<Category> findByName(String name);
    List<Category> findAll();
    void update(Category category);
    void delete(Long id);
}
