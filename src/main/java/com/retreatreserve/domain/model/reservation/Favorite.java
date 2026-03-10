package com.retreatreserve.domain.model.reservation;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import lombok.Getter;

@Getter
public class Favorite {
    private UUID id;
    private UUID userId;
    private UUID cabinId;
    private LocalDateTime addedAt;

    protected Favorite() {
    }

    public Favorite(UUID userId, UUID cabinId) {
        this.id = UUID.randomUUID();
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.cabinId = Objects.requireNonNull(cabinId, "Cabin ID cannot be null");
        this.addedAt = LocalDateTime.now();
    }

    public Favorite(UUID id, UUID userId, UUID cabinId, LocalDateTime addedAt) {
        this.id = id;
        this.userId = userId;
        this.cabinId = cabinId;
        this.addedAt = addedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(id, favorite.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
