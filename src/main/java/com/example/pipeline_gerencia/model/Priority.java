package com.example.pipeline_gerencia.model;

/**
 * Enum representing task priority levels
 */
public enum Priority {
    LOW(1, "Baixa"),
    MEDIUM(2, "Média"),
    HIGH(3, "Alta"),
    CRITICAL(4, "Crítica");

    private final int level;
    private final String description;

    Priority(int level, String description) {
        this.level = level;
        this.description = description;
    }

    public int getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }
}
