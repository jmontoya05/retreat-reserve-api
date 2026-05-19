package com.retreatreserve.domain.model.cabin;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import lombok.Getter;

@Getter
public class Feature {
    private UUID id;
    private String name;
    private String iconKey;
    private String description;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected Feature() {
    }

    public Feature(String name, String iconUrl, String description) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.iconKey = Objects.requireNonNull(iconUrl, "Icon URL cannot be null");
        this.description = description;
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    public Feature(UUID id, String name, String iconUrl, String description, Boolean active,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.iconKey = iconUrl;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateDetails(String name, String iconUrl, String description) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.iconKey = Objects.requireNonNull(iconUrl, "Icon URL cannot be null");
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feature feature = (Feature) o;
        return Objects.equals(id, feature.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
