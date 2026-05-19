package com.retreatreserve.domain.model.cabin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.retreatreserve.domain.exception.cabin.DuplicateFeatureException;
import com.retreatreserve.domain.exception.cabin.InvalidPriceException;

import lombok.Getter;

/**
 * Cabin entity - Aggregate Root for cabin management.
 */
@Getter
public class Cabin {
    private UUID id;
    private String name;
    private String description;
    private UUID categoryId;
    private Location location;
    private Capacity capacity;
    private Integer numberOfBedrooms;
    private Integer numberOfBathrooms;
    private BigDecimal pricePerNight;
    private BigDecimal averageRating;
    private Integer totalReviews;
    private CabinStatus status;
    private Boolean active;
    private final List<CabinImage> images;
    private final List<UUID> featureIds;
    private final List<Policy> policies;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ========== Constructors ==========

    protected Cabin() {
        this.images = new ArrayList<>();
        this.featureIds = new ArrayList<>();
        this.policies = new ArrayList<>();
        this.status = CabinStatus.AVAILABLE;
    }

    /**
     * Creates a new Cabin.
     */
    public Cabin(String name, String description, UUID categoryId, Location location,
                 Capacity capacity, Integer numberOfBedrooms, Integer numberOfBathrooms,
                 BigDecimal pricePerNight) {
        this();
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.description = Objects.requireNonNull(description, "Description cannot be null");
        this.categoryId = Objects.requireNonNull(categoryId, "Category ID cannot be null");
        this.location = Objects.requireNonNull(location, "Location cannot be null");
        this.capacity = Objects.requireNonNull(capacity, "Capacity cannot be null");
        this.numberOfBedrooms = Objects.requireNonNull(numberOfBedrooms, "Number of bedrooms cannot be null");
        this.numberOfBathrooms = Objects.requireNonNull(numberOfBathrooms, "Number of bathrooms cannot be null");
        setPricePerNight(pricePerNight);
        this.active = true;
        this.averageRating = BigDecimal.ZERO;
        this.totalReviews = 0;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Reconstitutes a Cabin from persistence.
     */
    public Cabin(UUID id, String name, String description, UUID categoryId, Location location,
                 Capacity capacity, Integer numberOfBedrooms, Integer numberOfBathrooms,
                 BigDecimal pricePerNight, BigDecimal averageRating, Integer totalReviews,
                 CabinStatus status, Boolean active, List<CabinImage> images,
                 List<UUID> featureIds, List<Policy> policies,
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.location = location;
        this.capacity = capacity;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.pricePerNight = pricePerNight;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
        this.status = status;
        this.active = active;
        this.images = images != null ? new ArrayList<>(images) : new ArrayList<>();
        this.featureIds = featureIds != null ? new ArrayList<>(featureIds) : new ArrayList<>();
        this.policies = policies != null ? new ArrayList<>(policies) : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ========== Domain Methods - Images ==========

    public void addImage(CabinImage image) {
        this.images.add(Objects.requireNonNull(image, "Image cannot be null"));
        this.updatedAt = LocalDateTime.now();
    }

    public void removeImage(UUID imageId) {
        this.images.removeIf(img -> img.getId().equals(imageId));
        this.updatedAt = LocalDateTime.now();
    }

    public void setPrimaryImage(UUID imageId) {
        this.images.forEach(img -> img.setIsPrimary(false));
        this.images.stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .ifPresent(img -> img.setIsPrimary(true));
        this.updatedAt = LocalDateTime.now();
    }

    public boolean hasRequiredImages() {
        return !this.images.isEmpty();
    }

    // ========== Domain Methods - Features ==========

    public void addFeature(UUID featureId) {
        if (hasFeature(featureId)) {
            throw new DuplicateFeatureException("Feature already exists for this cabin");
        }
        this.featureIds.add(featureId);
        this.updatedAt = LocalDateTime.now();
    }

    public void removeFeature(UUID featureId) {
        this.featureIds.removeIf(feat -> feat.equals(featureId));
        this.updatedAt = LocalDateTime.now();
    }

    public boolean hasFeature(UUID featureId) {
        return this.featureIds.contains(featureId);
    }

    // ========== Domain Methods - Policies ==========

    public void addPolicy(Policy policy) {
        this.policies.add(Objects.requireNonNull(policy, "Policy cannot be null"));
        this.updatedAt = LocalDateTime.now();
    }

    public void removePolicy(UUID policyId) {
        this.policies.removeIf(p -> p.getId().equals(policyId));
        this.updatedAt = LocalDateTime.now();
    }

    // ========== Domain Methods - Rating ==========

    public void updateRatingSummary(BigDecimal newAverageRating, Integer totalReviews) {
        this.averageRating = Objects.requireNonNull(newAverageRating, "Average rating cannot be null");
        this.totalReviews = Objects.requireNonNull(totalReviews, "Total reviews cannot be null");
        this.updatedAt = LocalDateTime.now();
    }

    // ========== Domain Methods - General ==========

    public void updatePricing(BigDecimal newPrice) {
        setPricePerNight(newPrice);
        this.updatedAt = LocalDateTime.now();
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        if (pricePerNight == null || pricePerNight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPriceException("Price must be greater than zero");
        }
        this.pricePerNight = pricePerNight;
    }

    public void disable() {
        this.status = CabinStatus.DISABLED;
        this.updatedAt = LocalDateTime.now();
    }

    public void enable() {
        this.status = CabinStatus.AVAILABLE;
        this.updatedAt = LocalDateTime.now();
    }

    public void markUnderMaintenance() {
        this.status = CabinStatus.UNDER_MAINTENANCE;
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

    public void updateBasicInfo(String name, String description, UUID categoryId,
                                Integer numberOfBedrooms, Integer numberOfBathrooms) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.description = Objects.requireNonNull(description, "Description cannot be null");
        this.categoryId = Objects.requireNonNull(categoryId, "Category ID cannot be null");
        this.numberOfBedrooms = Objects.requireNonNull(numberOfBedrooms, "Number of bedrooms cannot be null");
        this.numberOfBathrooms = Objects.requireNonNull(numberOfBathrooms, "Number of bathrooms cannot be null");
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isAvailableForBooking() {
        return this.active && this.status == CabinStatus.AVAILABLE;
    }

    // ========== Equals & HashCode ==========

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cabin cabin = (Cabin) o;
        return Objects.equals(id, cabin.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
