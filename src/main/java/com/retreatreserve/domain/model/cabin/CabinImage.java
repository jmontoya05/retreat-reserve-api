package com.retreatreserve.domain.model.cabin;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
public class CabinImage {
    private UUID id;
    private String imageUrl;
    private Integer displayOrder;
    @Setter
    private Boolean isPrimary;
    private LocalDateTime uploadedAt;

    protected CabinImage() {
    }

    public CabinImage(String imageUrl, Integer displayOrder, Boolean isPrimary) {
        this.id = UUID.randomUUID();
        this.imageUrl = Objects.requireNonNull(imageUrl, "Image url cannot be null");
        this.displayOrder = displayOrder != null ? displayOrder : 0;
        this.isPrimary = isPrimary != null ? isPrimary : false;
        this.uploadedAt = LocalDateTime.now();
    }

    public CabinImage(UUID id, String imageUrl, Integer displayOrder, Boolean isPrimary, LocalDateTime uploadedAt) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.displayOrder = displayOrder;
        this.isPrimary = isPrimary;
        this.uploadedAt = uploadedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CabinImage that = (CabinImage) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
