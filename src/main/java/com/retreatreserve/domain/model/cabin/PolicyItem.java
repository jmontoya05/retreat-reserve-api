package com.retreatreserve.domain.model.cabin;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import lombok.Getter;

@Getter
public class PolicyItem {
    private UUID id;
    private String description;
    private Integer displayOrder;
    private LocalDateTime createdAt;

    protected PolicyItem() {
    }

    public PolicyItem(String description, Integer displayOrder) {
        this.description = Objects.requireNonNull(description, "Description cannot be null");
        this.displayOrder = displayOrder != null ? displayOrder : 0;
        this.createdAt = LocalDateTime.now();
    }

    public PolicyItem(UUID id, String description, Integer displayOrder, LocalDateTime createdAt) {
        this.id = id;
        this.description = description;
        this.displayOrder = displayOrder;
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolicyItem that = (PolicyItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
