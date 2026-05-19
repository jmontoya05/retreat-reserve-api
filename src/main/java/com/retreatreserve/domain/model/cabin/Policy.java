package com.retreatreserve.domain.model.cabin;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import lombok.Getter;

@Getter
public class Policy {
    private UUID id;
    private String title;
    private Integer displayOrder;
    private final List<PolicyItem> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected Policy() {
        this.items = new ArrayList<>();
    }

    public Policy(String title, Integer displayOrder) {
        this();
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.displayOrder = displayOrder != null ? displayOrder : 0;
        this.createdAt = LocalDateTime.now();
    }

    public Policy(UUID id, String title, Integer displayOrder, List<PolicyItem> items,
                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.displayOrder = displayOrder;
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void addItem(PolicyItem item) {
        this.items.add(Objects.requireNonNull(item, "Item cannot be null"));
        this.updatedAt = LocalDateTime.now();
    }

    public void removeItem(UUID itemId) {
        this.items.removeIf(item -> item.getId().equals(itemId));
        this.updatedAt = LocalDateTime.now();
    }

    public void updateTitle(String title) {
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.updatedAt = LocalDateTime.now();
    }

    public List<PolicyItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Policy policy = (Policy) o;
        return Objects.equals(id, policy.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
