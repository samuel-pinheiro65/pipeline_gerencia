package com.example.pipeline_gerencia.model;

/**
 * Enum representing task status
 */
public enum Status {
    PENDING("Pendente"),
    IN_PROGRESS("Em Andamento"),
    BLOCKED("Bloqueada"),
    COMPLETED("Concluída"),
    CANCELLED("Cancelada");

    private final String description;

    Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
