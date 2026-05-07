package com.retreatreserve.infrastructure.adapter.out.persistence.entity;

import com.retreatreserve.domain.model.cabin.CabinStatus;
import com.retreatreserve.infrastructure.adapter.out.persistence.embeddable.LocationEmbeddable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JPA Entity for Cabin aggregate.
 * Owns: CabinImage, CabinFeature, Policy (with PolicyItems).
 */
@Entity
@Table(name = "cabins")
@Getter
@Setter
public class CabinJpaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "id", columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryJpaEntity category;
    
    @Embedded
    private LocationEmbeddable location;
    
    @Column(name = "max_guests", nullable = false)
    private Integer maxGuests;
    
    @Column(name = "number_of_bedrooms", nullable = false)
    private Integer numberOfBedrooms;
    
    @Column(name = "number_of_bathrooms", nullable = false)
    private Integer numberOfBathrooms;
    
    @Column(name = "price_per_night", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;
    
    @Column(name = "average_rating", precision = 3, scale = 2, nullable = false)
    private BigDecimal averageRating;
    
    @Column(name = "total_reviews", nullable = false)
    private Integer totalReviews;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CabinStatus status;
    
    @Column(nullable = false)
    private Boolean active;
    
    // Relationships - Cabin is the aggregate root
    @OneToMany(mappedBy = "cabin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CabinImageJpaEntity> images = new ArrayList<>();
    
    @OneToMany(mappedBy = "cabin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CabinFeatureJpaEntity> cabinFeatures = new ArrayList<>();
    
    @OneToMany(mappedBy = "cabin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PolicyJpaEntity> policies = new ArrayList<>();
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
