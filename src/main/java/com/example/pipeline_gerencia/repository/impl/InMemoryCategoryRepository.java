package com.example.pipeline_gerencia.repository.impl;

import com.example.pipeline_gerencia.model.Category;
import com.example.pipeline_gerencia.repository.CategoryRepository;
import java.util.*;

/**
 * In-memory implementation of CategoryRepository
 */
public class InMemoryCategoryRepository implements CategoryRepository {
    private final Map<Long, Category> categories = new HashMap<>();
    private long nextId = 1;

    @Override
    public Category save(Category category) {
        if (category.getId() == null) {
            category.setId(nextId++);
        }
        categories.put(category.getId(), category);
        return category;
    }

    @Override
    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(categories.get(id));
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categories.values().stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(categories.values());
    }

    @Override
    public void update(Category category) {
        if (category.getId() != null && categories.containsKey(category.getId())) {
            categories.put(category.getId(), category);
        }
    }

    @Override
    public void delete(Long id) {
        categories.remove(id);
    }
}
